/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

public class Items extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        Connection con=null;
        Statement st=null;
        PrintWriter out = response.getWriter();
        try {           
            
            Class.forName("org.postgresql.Driver");
            String url="jdbc:postgresql://localhost:5432/Services";
            String user="postgres";
            String pass="tejas";
            con=DriverManager.getConnection(url,user,pass);            
            st=con.createStatement();
            
            //GETTING INPUT FROM DYNAMIC ROWS
            String[] Chkbox=request.getParameterValues("iteChk");
            String[] Item_no=request.getParameterValues("iteNo");
            String[] Item_name=request.getParameterValues("ite");
            String delIteName=request.getParameter("dite");
            String updateIteNo=request.getParameter("uiteNo");
            String updateIteName=request.getParameter("uiteName");            
            
            String s2=request.getParameter("s1");            
            if(s2.equals("Save") && Chkbox!=null){                
                for(int i=0;i<Chkbox.length;i++){                
                    //st.executeUpdate("insert into Orders values('"+Item_no[i]+"','"+Item_desc[i]+"','"+Qty[i]+"','"+Rate[i]+"')");                                        
                    PreparedStatement pstmt;
                    pstmt=con.prepareStatement("insert into \"Items\" values(?,?)");
                    pstmt.setInt(1,Integer.parseInt(Item_no[i]));
                    pstmt.setString(2,Item_name[i]);                    
                    pstmt.executeUpdate();
                }
                out.print("<script language='JavaScript'>alert('Records Inserted');</script>");
            }
            
            else if(s2.equals("Display")){
                out.println("<center><h1>Records</h1></center>");
                ResultSet rs=st.executeQuery("select * from \"Items\"");

                while(rs.next()){
                    int iteno=rs.getInt(1);
                    String itename=rs.getString(2);                                        

                        out.println("<center><table>"+"<tr>"+"<td>"+"<input type=checkbox>"+"</td>"+"<td>"+"<input type=text value='"+iteno+"'>"+"</td>"+"<td>"+"<input type=text value='"+itename+"'>"+"</tr>");
                        out.println("</table></center>");

                }
            }
            
            else if(s2.equals("Delete")){
            String delQuery= "delete from \"Items\" where \"Item_name\"= '"+delIteName+"' ";
            st.executeUpdate(delQuery);
            out.println("<h1>Deleted "+delIteName+"</h1>");
            }
            
            else if(s2.equals("Update")){
            String updateQuery="update \"Items\" set \"Item_name\"='"+updateIteName+"' where \"Item_no\"='"+updateIteNo+"'";
            st.executeUpdate(updateQuery);
            out.println("<h1>Field Updated to "+updateIteName+"</h1>");
            }
            
            else{
                out.println("<center><h1>One or more records are not selected!</h1></center>");
            }
            
            
            st.close();
            con.close();
        }
        catch(SQLException se){
            out.println(se.getMessage());
        }
        catch(Exception e){
            out.println(e.getMessage());
        }
    }
}