<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
  <div class="page">
    <h1>Web Checkers</h1>
    
    <div class="navigation">
      <a href="/">home</a>
    </div>
    
    <div class="body">
      <h2>Sign in:</h2>
      
      <#if message??>
      <div class="error">${message}</div>
      </#if>
      
      <form action="/signin" method="POST">
        Your name: <input name="name" />
        <br/>
        <button type="submit">Sign-In Casual Play Mode</button>
        <button type="submit">Sign-In Tournament Play Mode</button>
      </form>
    </div>
    
  </div>
</body>
</html>
