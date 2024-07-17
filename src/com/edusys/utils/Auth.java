/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.utils;

import com.edusys.entity.NhanVien;

/**
 *
 * @author Admin
 */
public class Auth {

    /**
     *
     */
    public static NhanVien USER = null;

    public static void clear() {
        Auth.USER = null;
    }
    public static boolean isLogin() {
        return Auth.USER != null;
    }
    public static boolean isManager() {
        return Auth.isLogin()&& USER.getVaiTro();
    }
}
