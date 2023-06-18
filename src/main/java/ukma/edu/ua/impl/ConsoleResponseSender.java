package ukma.edu.ua.impl;

import ukma.edu.ua.model.ResponseSender;

public class ConsoleResponseSender implements ResponseSender {
    @Override
    public void accept(Object o) {
        System.out.println(o);
    }
}
