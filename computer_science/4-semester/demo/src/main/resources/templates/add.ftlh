<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<fieldset>
    <form method="post">
        <input name="name" placeholder="name" type="text">
        <input name="price" placeholder="price" type="number">
        <#if categories??>
            <#list categories as category>
                <label> ${category.name}
                    <input type="checkbox" value="${category}">
                </label>
            </#list>
        </#if>
        <button type="submit"/>
    </form>
</fieldset>
</body>
</html>