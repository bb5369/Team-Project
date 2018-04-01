<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="refresh" content="10">
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
  <div class="page">
  
    <h1>Web Checkers</h1>
    
    <div class="navigation">
        <a href="/">home</a>
        <#if currentPlayer??>
                <a href="/signout" style="float: right";>sign out</a>
        </#if>
    </div>


    <div class="body">
        <#if currentPlayer??>

            <p>Welcome, ${currentPlayer.name}.</p>

            <h2>Online Players</h2>



            <#if message??>
            <div id="message" class="${message.type}">${message.text}</div>
            <#else>
            <div id="message" class="info" style="display:none">
            </div>
            </#if>

            <ul>
            <#list activePlayers?keys as key>
            <#if activePlayers[key].name != currentPlayer.name>
                <li><a href="${gameRoute}?whitePlayer=${activePlayers[key].name}">${activePlayers[key].name}</a></li>
            </#if>
            </#list>
            </ul>
        <#else>
            <p>Welcome to the world of online Checkers.</p>
        </#if>

        <#if activePlayerCount??>
        <p>There are currently ${activePlayerCount} active players on the server.</p>
        </#if>

        <#if ! currentPlayer??>
        <div class="signin">
            <button class="sign_btn" onclick="window.location.href='${signInUrl}'" type="button" >Sign-in to play a game!</button>
        </div>
        </#if>

    </div>
  </div>
</body>
</html>
