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
      <h2>Tournament Standings:</h2>

        <ol>
        <#list players as player>
            <li>${player.getName()} - ${player.getWins()}</li>
            <br>
        </#list>
        </ol>
    </div>

  </div>
</body>
</html>
