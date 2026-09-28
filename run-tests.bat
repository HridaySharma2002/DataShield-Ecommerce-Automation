@echo off
setlocal
cd /d "%~dp0"
echo ==========================================================
echo  DataShield Automation - Quick Runner for Proof of Work
echo ==========================================================
powershell -ExecutionPolicy Bypass -File "%~dp0run-tests.ps1" %*
echo.
pause
