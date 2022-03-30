$parent = Split-Path -Parent $MyInvocation.MyCommand.Definition;

$dbapi_home = $parent + "\..\";
$lib_dir = $dbapi_home + "lib\";
$conf_dir = $dbapi_home + "conf";

$command = "java -cp '" + $conf_dir;

$exclude_jars = "spring-boot-starter-webflux", "spring-webflux", "spring-cloud-gateway-server", "spring-cloud-starter-gateway";

# 判断是否包含在exclude jar中
Function contain($jar)
{
    $res = 0
    foreach ($i in $exclude_jars)
    {
        if ($jar.indexof($i) -eq 0)
        {
            $res = 1;
            break;
        }
    }
    $res;
}

Get-ChildItem $lib_dir | ForEach-Object -Process{
    if ($_ -is [System.IO.FileInfo])
    {
        $tmp = contain($_.name);
        if ($tmp -eq 0)
        {
            $command = $command + ";" + $lib_dir + $_.name;
        }
    }
}
$command = $command + "' com.gitee.freakchicken.dbapi.DBApiStandalone";

cd $dbapi_home;
iex $command;