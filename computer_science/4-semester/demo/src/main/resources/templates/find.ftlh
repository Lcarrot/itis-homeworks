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
        <input name="name" type="text">
        <input name="price" type="number">
        <button type="submit"/>
    </form>
</fieldset>
<tr>
    <#if goods??>
        <#list goods as good>
            <td>${good.name}</td>
            <td>${good.price}</td>
            <#if good.categories??>
                <#list good.categories as category>
                    <td>${category.name}</td>
                </#list>
            </#if>
            <br>
        </#list>
    </#if>
</tr>
</body>
</html>