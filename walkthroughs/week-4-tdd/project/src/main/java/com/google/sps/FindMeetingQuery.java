// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    
    // If there are no attendees in the request, return the entire day
    if (request.getAttendees().isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }
      
    // If duration of the request is longer than a day, return an empty array
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return Arrays.asList();
    }

    // List of all unavailable times
    ArrayList<TimeRange> unavailableTimes = new ArrayList<>();

    // Get list of attendees on meeting request
    Collection<String> reqAttendees = request.getAttendees();
    for (String reqAttendee : reqAttendees) {

      // Check whether attendee is scheduled for other events
      for (Event event : events) {
        if (event.getAttendees().contains(reqAttendee)) {

          // Add time range of event to list of unavailable time ranges
          unavailableTimes.add(event.getWhen());
        }
      }
    }

    // If there are no conflicts, return the entire day
    if (unavailableTimes.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    // Sort list of unavailable times from earliest to latest
    Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);

    // Remove nested time ranges and combine overlapping time ranges in list
    int i = 0;
    while (i < unavailableTimes.size()-1) {
      TimeRange currTimeRange = unavailableTimes.get(i);
      TimeRange nextTimeRange = unavailableTimes.get(i+1);

      if (currTimeRange.contains(nextTimeRange)) {
        unavailableTimes.remove(nextTimeRange);
      } else if (nextTimeRange.contains(currTimeRange)) {
        unavailableTimes.remove(currTimeRange);
      } else if (currTimeRange.overlaps(nextTimeRange)) {
        TimeRange newTimeRange = 
            TimeRange.fromStartEnd(currTimeRange.start(), nextTimeRange.end(), false);
        unavailableTimes.remove(currTimeRange);
        unavailableTimes.remove(nextTimeRange);
        unavailableTimes.add(i, newTimeRange);
      } else {
        i++;
      }
    }

    // List of all available times
    ArrayList<TimeRange> availableTimes = new ArrayList<>();

    // Create time ranges using gaps in between unavailable times
    for (int j = 0; j < unavailableTimes.size(); j++) {
      TimeRange timeRange = unavailableTimes.get(j);
      TimeRange prevTimeRange;
      TimeRange newTimeRange;

      if (j == 0) {
        newTimeRange = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, timeRange.start(), false);
      } else {
        prevTimeRange = unavailableTimes.get(j-1);
        newTimeRange = TimeRange.fromStartEnd(prevTimeRange.end(), timeRange.start(), false);
      }

      // Add to list of available times if gap between events is large enough
      if (newTimeRange.duration() >= request.getDuration()) {
        availableTimes.add(newTimeRange);
      }
      
      // Time range is last on the list
      if (j == unavailableTimes.size()-1) {
        newTimeRange = TimeRange.fromStartEnd(timeRange.end(), TimeRange.END_OF_DAY, true);
        if (newTimeRange.duration() >= request.getDuration()) {
          availableTimes.add(newTimeRange);
        }
      }
    }

    return availableTimes;
  }
}
