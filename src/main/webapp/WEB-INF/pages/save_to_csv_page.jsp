<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Save to CSV</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>
<body>
<div class="container">
    <h3><img height="50" width="55" src="<c:url value="/static/logo.png"/>"/><a href="/">Contacts List</a></h3>
    <div>
        <h4>Contacts have been saved</h4>
    </div>
    <div class="form-group"><input type="submit" class="btn btn-primary" value="Back to main page" id="backToMainPage">
    </div>
    </form>
</div>

<script>
    $('#backToMainPage').click(function () {
        window.location.href = '/';
    });
</script>
</body>
</html>