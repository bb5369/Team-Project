<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
  <div class="page">
  
    <h1>Web Checkers</h1>
    
    <div class="navigation">
      <a href="/">my home</a>
    </div>
    
    <div class="body">
        <p>Welcome to the world of online Checkers.</p>
        <div class="signin">
            <h4> Click this button to get started!</h4>
            <button class="sign_btn" onclick="window.location.href='signin'" type="button" >Sign in</button>
        </div>
        <#if activePlayerCount??>
            <p>There are currently ${activePlayerCount} active players on the server.</p>
        </#if>
    </div>
    
  </div>
</body>
</html>
