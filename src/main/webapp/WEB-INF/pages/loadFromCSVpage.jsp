<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Read from CSV</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>
<body>
<div class="container">
    <h3><img height="50" width="55" src="<c:url value="/static/logo.png"/>"/><a href="/">Contacts List</a></h3>
    <form class="form-inline" action="/loadCSV" method="post">
        <div class="form-group mb-2">
            <input type="file" class="form-control" name="filePath">
        </div>
        <div class="form-group mb-2">
            <button type="submit" class="btn" id="loadFromFile">Submit</button>
        </div>
        <div>
            <button type="button" class="btn btn-primary form-control" id="backToMainPage">Back to main page
            </button>
        </div>
    </form>
</div>

<script>
    $('#backToMainPage').click(function () {
        window.location.href = '/';
    });
    $('#loadFromFile').click(function () {
        window.location.href = '/loadFromCSVsuccess';
    });
</script>
</body>
</html>