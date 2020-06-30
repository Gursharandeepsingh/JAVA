package digitalfoodordering;
import com.vmm.JHTTPServer;
import java.sql.*;
import java.util.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;



public class serverForFoodOrdering extends JHTTPServer{

    public serverForFoodOrdering(int port) throws IOException { 
        // TODO code application logic here
        super(port);
    }
    public Response serve(String uri,String method,Properties header,Properties parms,Properties files)
    {
        Response res = null;
        System.out.println("URI "+uri);
        if(uri.contains("/AdminLogin"))
        {
            String un=parms.getProperty("username");
            String pw=parms.getProperty("password");
            System.out.println(un+"--"+pw);
            String ans = "";
            ResultSet rs = DBLoader.executeQuery("select * from admin where username=\'"+un+"\' and password =\'"+pw+"\'");
            try 
            {
                if(rs.next())
                {
                    ans = "success";
                }
                else
                {
                    ans = "failed";
                }
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK,"text/plain",ans);

        }
        else if(uri.contains("/AdminChangedPassword"))
        {
            String un=parms.getProperty("username");
            String oldpass=parms.getProperty("oldpassword");
            String newpass=parms.getProperty("newpassword");
            System.out.println(un+"--"+oldpass+"--"+newpass);
            String ans = "";
            ResultSet rs = DBLoader.executeQuery("select * from admin where username=\'"+un+"\' and password =\'"+oldpass+"\'");
            try 
            {
                if(rs.next())
                {
                    rs.updateString("password",newpass);
                    rs.updateRow();
                    ans = "success";
                }
                else
                {
                    ans = "failed";
                }
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK,"text/plain",ans);

            }
        else if(uri.contains("/AddCategory"))
        {
            String cuisine=parms.getProperty("cuisine");
            String category=parms.getProperty("category");
            String description=parms.getProperty("description");
            String photopath = saveFileOnServerWithRandomName(files, parms, "photo", "src/content");
            String ans = "";
            System.out.println(cuisine+"--"+category+"--"+description+"--"+photopath);
            ResultSet rs = DBLoader.executeQuery("select * from category where name=\'"+category+"\'");
            try 
            {
                if(rs.next())
                {
                    ans="failed";
                }
                else
                {
                    rs.moveToInsertRow();
                    rs.updateString("name",category);
                    rs.updateString("description",description);
                    rs.updateString("photo","src/content/" + photopath);
                    rs.updateString("cuisine",cuisine);
                    rs.insertRow();
                    ans = "success";
                }
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK,"text/plain",ans);

        }
        else if (uri.contains("/GetResource")) //request should be of the form /GetResource/one.jpg
        {
            uri = uri.substring(1);
            uri = uri.substring(uri.indexOf("/") + 1);
            System.out.println(uri +" *** " );
            res = sendCompleteFile(uri);
        } 
        else if(uri.contains("/GetCategoryByCuisine"))
        {
            String cuisine=parms.getProperty("cuisine");
            String ans = "";
            System.out.println(cuisine);
            ResultSet rs = DBLoader.executeQuery("select * from category where cuisine=\'"+cuisine+"\'");
             try {
                while (rs.next()) {
                   ans += rs.getString("name") + "#$%" +                  //Next Column
                    rs.getString("description") + "#$%" +                 //Next column
                    rs.getString("photo") + "~!@";                        //Next Row 
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/html", ans);
        } 
        else if(uri.contains("/DeleteRowOfCategory"))
        {
            String currentcategory=parms.getProperty("currentcategory");
            String ans = "";
            ResultSet rs = DBLoader.executeQuery("select * from category where name=\'"+currentcategory+"\'");
            try 
            {
                if(rs.next())
                {
                    rs.deleteRow();
                    ans = "success";
                }
                else
                {
                    ans = "failed";
                }
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK,"text/plain",ans);

            }
        else if(uri.contains("/AddFoodItem"))
        {
            String cuisine=parms.getProperty("cuisine");
            String category=parms.getProperty("category");
            String description=parms.getProperty("description");
            String itemname=parms.getProperty("itemname");
            String type=parms.getProperty("type");
            String price=parms.getProperty("price");
            String photopath = saveFileOnServerWithRandomName(files, parms, "photo", "src/content");
            String ans = "";
            System.out.println(cuisine+"--"+category+"--"+itemname+"--"+description+"--"+type+"--"+price+"--"+photopath);
            ResultSet rs = DBLoader.executeQuery("select * from fooditem where category=\'"+category+"\' and itemname=\'"+itemname+"\'");
            try 
            {
                if(rs.next())
                {
                    ans="failed";
                }
                else
                {
                    rs.moveToInsertRow();
                    rs.updateString("category",category);
                    rs.updateString("itemname",itemname);
                    rs.updateString("type",type);
                    rs.updateString("description",description);
                    rs.updateString("photo","src/content/" + photopath);
                    rs.updateString("price",price);
                    rs.insertRow();
                    ans = "success";
                }
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK,"text/plain",ans);

        }
         else if(uri.contains("/GetFoodItemByCategory"))
        {
            String category=parms.getProperty("category");
            String ans = "";
            System.out.println(category);
            ResultSet rs = DBLoader.executeQuery("select * from fooditem where category=\'"+category+"\'");
             try {
                while (rs.next()) {
                   ans += rs.getString("itemname") + "#$%" +                  //Next Column
                    rs.getString("description") + "#$%" +                 //Next column
                    rs.getString("price") + "#$%" +                       //Next column
                    rs.getString("type") + "#$%" +                        //Next column
                    rs.getString("photo") + "~!@";                        //Next Row 
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/html", ans);
        } 
         else if(uri.contains("/DeleteRowOfFoodItem"))
        {
            String currentfooditem=parms.getProperty("currentfooditem");
            String ans = "";
            ResultSet rs = DBLoader.executeQuery("select * from fooditem where itemname=\'"+currentfooditem+"\'");
            try 
            {
                if(rs.next())
                {
                    rs.deleteRow();
                    ans = "success";
                }
                else
                {
                    ans = "failed";
                }
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK,"text/plain",ans);

            }
       
        else {
            res = new Response(HTTP_OK, "text/html", "<body style='background: #D46A6A; color: #fff'><center><h1>Hello</h1><br> <h3>Welcome To JHTTP Server (Version 1.0)</h3></body></center>");
        }
  
        return res;
    }   

}
    

