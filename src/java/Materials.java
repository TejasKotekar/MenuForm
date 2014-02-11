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

public class Materials extends HttpServlet {

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
            String[] Chkbox=request.getParameterValues("matChk");
            String[] Material_no=request.getParameterValues("matNo");
            String[] Material_name=request.getParameterValues("mat");
            String delMatName=request.getParameter("dmat");
            String updateMatNo=request.getParameter("umatNo");
            String updateMatName=request.getParameter("umatName");
            String s2=request.getParameter("s1");    
            
            if(s2.equals("Save") && Chkbox!=null){                
                for(int i=0;i<Chkbox.length;i++){                
                    //st.executeUpdate("insert into Orders values('"+Item_no[i]+"','"+Item_desc[i]+"','"+Qty[i]+"','"+Rate[i]+"')");                                        
                    PreparedStatement pstmt;
                    pstmt=con.prepareStatement("insert into \"Materials\" values(?,?)");
                    pstmt.setInt(1,Integer.parseInt(Material_no[i]));
                    pstmt.setString(2,Material_name[i]);                    
                    pstmt.executeUpdate();
                }
                out.print("<script language='JavaScript'>alert('Records Inserted');</script>");
            }
            
            else if(s2.equals("Display")){
                out.println("<center><h1>Records</h1></center>");
                ResultSet rs=st.executeQuery("select * from \"Materials\"");

                while(rs.next()){
                    int matno=rs.getInt(1);
                    String matname=rs.getString(2);                                        

                        out.println("<center><table>"+"<tr>"+"<td>"+"<input type=checkbox>"+"</td>"+"<td>"+"<input type=text value='"+matno+"'>"+"</td>"+"<td>"+"<input type=text value='"+matname+"'>"+"</tr>");
                        out.println("</table></center>");

                }
            }
            
            else if(s2.equals("Delete")){
            String delQuery= "delete from \"Materials\" where \"Material_name\"= '"+delMatName+"' ";
            st.executeUpdate(delQuery);
            out.println("<h1>Deleted "+delMatName+"</h1>");
            }

            else if(s2.equals("Update")){
            String updateQuery="update \"Materials\" set \"Material_name\"='"+updateMatName+"' where \"Material_no\"='"+updateMatNo+"'";
            st.executeUpdate(updateQuery);
            out.println("<h1>Field Updated to "+updateMatName+"</h1>");
            }
            
            else{
                out.println("<center><h1>One or more records are not selected!</h1></center>");
            }
            
            String s3=request.getParameter("s1");
            if(s3.equals("DELETE")){
                for(String s : Chkbox){
                    
                }
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