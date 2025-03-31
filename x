<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Add Coffee</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <h1>Add Coffee</h1>
    <form method="post" action="/coffee/add">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" th:value="${coffee.name}" required>
        <label for="description">Description:</label>
        <input type="text" id="description" name="description" th:value="${coffee.description}" required>
        <button type="submit">Save</button>
    </form>
    <a href="/coffee">Back</a>
</body>
</html>
