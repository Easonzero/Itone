package com.wangyi.define;

public class EventName {
	public static class Download{
		public static final int WAITING = 0;
		public static final int STARTED = 1;
		public static final int FINISHED = 2;
		public static final int STOPPED = 3;
		public static final int ERROR = 4;
	}
	public static class Https{
		public static final String OK = "ok";
		public static final String ERROR = "error";
		public static final String REGISTERFORM = "register";
		public static final String MODIFYFORM = "modify";
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
	public static class Message{
		public static final String HOMEWORK = "homework";
		public static final String MESSAGE = "message";
	}
}
