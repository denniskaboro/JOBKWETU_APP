package com.e.jobkwetu.App;

public class EndPoints {
    public static final String userid = MyApplication.getInstance().getPrefManager().getUser().getId();
    public static final String phone = MyApplication.getInstance().getPrefManager().getUser().getPhone();

    public static final String BASE_URL = "https://jobkwetu.com/api";
    //public static final String BASE_URL = "http://192.168.43.54:8000/api";
    public static final String LOGIN = BASE_URL + "/login";
    public static final String REGISTER = BASE_URL + "/register";
    public static final String MARKER = BASE_URL+"/markers";
    public static final String CATEGORY = BASE_URL+"/category";
    public static final String TASKER = BASE_URL+"/tasker";
    public static final String MARKER2 = BASE_URL+"/marker";
    public static final String HISTORY = BASE_URL+"/history/"+userid;
    public static final String TASKERS_LIST = BASE_URL+"/taskers_list";
    public static final String LOGOUT = BASE_URL + "/logout";
    public static final String RESET_PASSWORD = BASE_URL + "/password/create";
    public static final String SUBCOUNTY = BASE_URL+"/subcounty";
    public static final String JOBBER = BASE_URL+"/jobber";
    public static final String TASKS_LIST = BASE_URL+"/tasks_list";
    public static final String TASK_FORM = BASE_URL+"/task_form";
    public static final String USER = BASE_URL + "/user/_ID_";
    public static final String PROFILE = BASE_URL + "/profile";
    public static final String NAME = BASE_URL + "/name";
    public static final String POPULAR = BASE_URL + "/popular";
    public static final String NEWLY = BASE_URL+"/newlyjoined";
    public static final String INSPIRED = BASE_URL+"/inspired";
    public static final String DETAILED = BASE_URL+"/Detailed";
    public static final String MPESA = BASE_URL+"/v1/hlab/stk/push";
    public static final String MPESABALANCE = BASE_URL+"/Balance/"+ userid;
    public static final String TRANSATION =BASE_URL+"/Transaction/"+ phone;
    public static final String CHAT_ROOMS = BASE_URL+"/Chats";
    public static final String CHAT_THREAD = BASE_URL + "/chat_rooms/_ID_";
    public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/chat_rooms/2/message";
}
