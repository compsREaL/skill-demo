//package com.real.skill.util;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.real.skill.domain.SkillUser;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author: mabin
// * @create: 2019/5/16 20:50
// */
//public class UserUtil {
//
//    private static void createUser(int count) throws Exception{
//        List<SkillUser> users = new ArrayList<>();
//        //生成用户
//        for (int i=0;i<count;i++){
//            SkillUser user = new SkillUser();
//            user.setId(13000000000L+i);
//            user.setLoginCount(1);
//            user.setNickname("user"+i);
//            user.setRegisterDate(new Date());
//            user.setSalt("1a2b3c");
//            user.setPassword(MD5Util.inputPassToDBPass("123456",user.getSalt()));
//            users.add(user);
//        }
//        System.out.println("create user");
////        //插入数据库
////        Connection conn = DBUtil.getConn();
////        String sql = "insert into skill_user(login_count,nickname,register_date,salt,password,id) values(?,?,?,?,?,?)";
////        PreparedStatement pstmt = conn.prepareStatement(sql);
////        for (int i=0;i<users.size();i++){
////            SkillUser user = users.get(i);
////            pstmt.setInt(1,user.getLoginCount());
////            pstmt.setString(2,user.getNickname());
////            pstmt.setTimestamp(3,new Timestamp(user.getRegisterDate().getTime()));
////            pstmt.setString(4,user.getSalt());
////            pstmt.setString(5,user.getPassword());
////            pstmt.setLong(6,user.getId());
////            pstmt.addBatch();
////        }
////        pstmt.executeBatch();
////        pstmt.close();
////        conn.close();
////        System.out.println("insert to db");
//        //登录，生成token
//        String urlString = "http://localhost:8080/login/do_login";
//        File file = new File("C:/tokens.txt");
//        if (file.exists()){
//            file.delete();
//        }
//        RandomAccessFile raf = new RandomAccessFile(file,"rw");
//        file.createNewFile();
//        raf.seek(0);
//        for (int i=0;i<users.size();i++){
//            SkillUser user = users.get(i);
//            URL url = new URL(urlString);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            OutputStream out = connection.getOutputStream();
//            String params = "mobile="+user.getId()+"&password="+MD5Util.inputPassToFormPass("123456");
//            out.write(params.getBytes());
//            out.flush();
//            InputStream in = connection.getInputStream();
//            ByteArrayOutputStream bout = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = in.read(buffer))!=-1){
//                bout.write(buffer,0,len);
//            }
//            in.close();
//            bout.close();
//            String response = new String(bout.toByteArray());
//            JSONObject jsonObject = JSON.parseObject(response);
//            String token = jsonObject.getString("data");
//            System.out.println("create token: "+ user.getId());
//
//            String row = user.getId()+","+token;
//            raf.seek(raf.length());
//            raf.write(row.getBytes());
//            raf.write("\r\n".getBytes());
//            System.out.println("write to file :"+user.getId());
//        }
//        raf.close();
//        System.out.println("over");
//    }
//
//    public static void main(String[] args) throws Exception {
//        createUser(5000);
//    }
//}
