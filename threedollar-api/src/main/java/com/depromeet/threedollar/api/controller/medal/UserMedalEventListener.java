package com.depromeet.threedollar.api.controller.medal;

import com.depromeet.threedollar.api.service.medal.UserMedalEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMedalEventListener {

    private final UserMedalEventService userMedalEventService;

}
