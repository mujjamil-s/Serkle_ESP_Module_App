package com.almaharapvtltd.serkle;

import com.tinder.scarlet.Stream;
import com.tinder.scarlet.ws.Receive;
import com.tinder.scarlet.ws.Send;


public interface TextCommunication{
    @Send
    void sendText(char message);

    @Send
    void sendText(String message);

    @Receive
    Stream<String> receiveText();
}
