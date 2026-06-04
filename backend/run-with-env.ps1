param(
    [string]$EnvFile = ".env",
    [string]$GradleTask = "bootRun"
)

$ErrorActionPreference = "Stop"

function Set-EnvFromFile {
    param([string]$Path)

    if (-not (Test-Path -LiteralPath $Path)) {
        throw "Env file not found: $Path"
    }

    Get-Content -LiteralPath $Path | ForEach-Object {
        $line = $_.Trim()

        if ([string]::IsNullOrWhiteSpace($line)) { return }
        if ($line.StartsWith("#")) { return }

        $parts = $line.Split("=", 2)
        if ($parts.Count -ne 2) { return }

        $key = $parts[0].Trim()
        $value = $parts[1].Trim()

        if ([string]::IsNullOrWhiteSpace($key)) { return }

        if (($value.StartsWith('"') -and $value.EndsWith('"')) -or ($value.StartsWith("'") -and $value.EndsWith("'"))) {
            $value = $value.Substring(1, $value.Length - 2)
        }

        [System.Environment]::SetEnvironmentVariable($key, $value, "Process")
    }
}

Set-EnvFromFile -Path $EnvFile

Write-Host "Loaded env from $EnvFile"
if ($env:APP_LLM_WEEKLY_REPORT_API_KEY) {
    Write-Host "APP_LLM_WEEKLY_REPORT_API_KEY is set (length=$($env:APP_LLM_WEEKLY_REPORT_API_KEY.Length))"
} else {
    Write-Host "APP_LLM_WEEKLY_REPORT_API_KEY is not set"
}
if ($env:OPENAI_API_KEY) {
    Write-Host "OPENAI_API_KEY is set (length=$($env:OPENAI_API_KEY.Length))"
} else {
    Write-Host "OPENAI_API_KEY is not set"
}

& .\gradlew.bat $GradleTask
