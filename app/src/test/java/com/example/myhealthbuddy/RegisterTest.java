package com.example.myhealthbuddy;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterTest {

    @Test
    public void isValid() {
        String Pass1="123456789";
        String Pass2="123456789";
        Register newacc=new Register();
        boolean result=newacc.isValid(Pass1,Pass2);
        assertEquals(false,result);
    }
    //Checks if password is 8 or more characters
    //Contains capital,small letters and a number

    @Test
    public void nationalIDValid() {
        String Nid="1102612679";
        Register newacc=new Register();
        boolean result=newacc.NationalIDValid(Nid);
        assertEquals(true,result);
    }

    @Test
    public void notEmpty() {
        String Email="Raghad@gmail.com";
        String Name="Raghad";
        String NID="";
        String phone="0501122332";
        String password="ASqw123456";
        String password2="ASqw123456";
        Register newacc=new Register();
        boolean result=newacc.notEmpty(Email,  Name,  NID,  phone, password, password2);
        assertFalse(result);
    }
}