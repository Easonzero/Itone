package com.wangyi.define;

public class EventName {
	public static class ScheduleFunc{
		public static final String ADDLESSON = "addlesson";
		public static final String CHANGELESSON = "changelesson";
		public static final String DELELESSON = "delelesson";
		public static final String FINDBYWEEK = "findbyweek";
		public static final String FINDBYDAY = "findbyday";
		public static final String FINDBYCLASS = "findbyclass";
	}
	public static class BookFunc{
	}
	public static class Https{
		public static final String OK = "ok";
		public static final String ERROR = "error";
	}
	public static class SensorFunc{
		public static final String SENSOR = "sensor";
	}
	public static class UI{
		public static final int START = 0;
		public static final int FINISH = 1;
		public static final int SUCCESS = 2;
		public static final int FAULT = 3;
	}
	public static class ImagePicker{
		public static final int IMAGE_REQUEST_CODE = 0;
		public static final int SELECT_PIC_KITKAT = 3;
		public static final int CAMERA_REQUEST_CODE = 1;
		public static final int RESULT_REQUEST_CODE = 2;
	}
}
