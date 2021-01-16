package com.jkkg.hhtx.event;

public class EventImpl {
    public static class loginOut implements IEvent{
        @Override
        public int getTag() {
            return 0;
        }
    }


    public static class RefreshInformation implements IEvent{

        @Override
        public int getTag() {
            return 0;
        }
    }

    public static class goToOne implements IEvent{

        @Override
        public int getTag() {
            return 0;
        }
    }
}
