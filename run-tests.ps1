param(
    [string]$suite = "regression"
)

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " DataShield E-Commerce & DB Validation Automation Runner " -ForegroundColor Yellow
Write-Host "==========================================================" -ForegroundColor Cyan

$xmlFile = "src/test/resources/testng-regression.xml"
if ($suite -eq "smoke") {
    $xmlFile = "src/test/resources/testng-smoke.xml"
} elseif ($suite -eq "db") {
    $xmlFile = "src/test/resources/testng-db-validation.xml"
}

Write-Host "Executing Test Suite: $xmlFile..." -ForegroundColor Green

$jars = (Get-ChildItem -Path "$env:USERPROFILE\.m2\repository" -Recurse -Filter "*.jar" | Where-Object { $_.FullName -notmatch "4\.14\.1" -and $_.FullName -notmatch "4\.18\.1" } | Select-Object -ExpandProperty FullName) -join ";"
$cp = "target/classes;target/test-classes;$jars"

java -cp $cp org.testng.TestNG $xmlFile 2>$null

Write-Host "`nTest Execution Finished!" -ForegroundColor Green
Write-Host "Capturing ExtentReports Dashboard screenshots..." -ForegroundColor Cyan

java -cp $cp com.datashield.automation.utils.ReportScreenshotTaker 2>$null

Write-Host "`nAll Proof of Work Screenshots Saved in 'screenshots\' folder!" -ForegroundColor Green
Write-Host "Extent Reports Dashboard: test-output\ExtentReport.html" -ForegroundColor Yellow

if (Test-Path "test-output\ExtentReport.html") {
    Start-Process "test-output\ExtentReport.html"
}
if (Test-Path "screenshots") {
    Start-Process "screenshots"
}
