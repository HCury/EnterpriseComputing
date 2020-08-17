<%-- <!-- /*  Name:    Henrique Cury
Course: C NT 4714 – S pring 2020 – Project Four
Assignment title:  A Three-Tier Distributed Web-Based Application
Date:  April 5, 2020 */ --> --%>
<!doctype html>
<%
    String textBox = (String) session.getAttribute("textBox");
    String result = (String) session.getAttribute("result");
    if(result == null){
        result = " ";
   }
   if(textBox == null){
       textBox = " ";
   }
%>


<html lang="en">

<body>
<style>table{
  margin-left:auto;
  margin-right:auto;
}</style>

<style>h1 { color : yellow; text-align: center;} body{; text-align: center; background-color: blue} textarea{background-color: black; color: green; font-size: 15}
button{background-color: black; color:yellow;} </style>

    <div class="container-fluid ">
        <row class="row justify-content-center">
            <h1 class="text-center col-sm-12 col-md-12 col-lg-12">Welcome to the Spring 2020 Project 4 Enterprise System </h1>
            <h2 class="text-center col-sm-12 col-md-12 col-lg-12">A Remote Database Management System </h2>
            <div class="text-center col-sm-12 col-md-12 col-lg-12"> You are connected to the Project 4 Enterprise System database.</div>
            <div class="text-center col-sm-12 col-md-12 col-lg-12"> Please enter any valid SQL query or update statement.</div>
            <div class="text-center col-sm-12 col-md-12 col-lg-12"> If no query/update command is initially provided the Execute button will display all supplier information in the database.</div>
            <div class="text-center col-sm-12 col-md-12 col-lg-12">All execution results will appear below</div>
            <form action = "/Project4/MySQLServlet" method = "post" style="margin-top: 15px;" class="text-center">
                <div class="form-group row">
                    <div class=" col-sm-12 col-md-12 col-lg-12">
                        <textarea name="textBox" class="form-control" id="textBox" rows="8" cols="50"><%= textBox %></textarea>
                    </div>
                </div>

                <button style="margin-bottom: 15px;" type="submit" class="btn btn-dark">Execute Command</button>
                <button onClick="reset();" style="margin-bottom: 15px;" type="reset" class="btn btn-dark">Clear Form</button>
            </form>
        </row>
    </div>


    <table>
     <%-- JSP expression to access servlet variable: message --%>
     <%=result%>
    </table>

    </div>

</html>
