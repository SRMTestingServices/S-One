
# 🚀 SOne BDD Automation Framework

A robust Java-based Maven Test Automation Framework that supports **Web (UI)**, **API**, and **Mobile (Android/iOS)** testing.  
This framework is built using **Cucumber BDD**, with support for **custom HTML reporting**, parallel execution, and easy extensibility.

---

## ✨ Features

- ✅ BDD with Cucumber
- 🧪 Supports Web UI testing (Selenium WebDriver)
- 📱 Supports Mobile Automation Android/iOS testing (Appium)
- 🔌 REST API testing using RestAssured
- 🖥️ Custom HTML Report with screenshots to support LifeSciences GxP process
- 🚀 Parallel Execution with Thread-safe WebDriver
- 🔁 Reusable Utility Libraries
- 📁 Page Object Model (POM) structure
- 🧠 Ready for GenAI integrations (future-ready)
- 🎯 Tag-based selective execution

---

## 🔧 Prerequisites

- Java 17+
- Maven 3.6+
- IntelliJ IDEA or Eclipse
- Android Studio (for mobile testing)
- Appium Server (GUI or CLI)
- Node.js & npm (for Appium CLI)
- ChromeDriver (for web testing)
- Android Emulator / Real Device connected

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/SRMTestingServices/S-One.git
cd <your-project>
```

### 2. Configure Application

```json
{
  "platform": "web",         
  "browser": "chrome",
  "appiumServerURL": "http://127.0.0.1:4723/wd/hub",
  "deviceName": "emulator-5554",
  "platformVersion": "13",
  "baseURL": "https://your-web-url.com",
  "apiBaseURL": "https://your-api.com"
}
```

---

### 🛠️ Appium Setup (for Mobile Automation)

#### Install Node.js and npm

Download and install from [https://nodejs.org/](https://nodejs.org/).  
Then verify:

```bash
node -v
npm -v
```

#### Install Appium CLI

```bash
npm install -g appium
```

Verify installation:

```bash
appium -v
```

> 💡 Optional: Install Appium Doctor to check system dependencies
```bash
npm install -g appium-doctor
appium-doctor
```

#### Install Android Studio

- Download from [https://developer.android.com/studio](https://developer.android.com/studio)
- During setup, install:
    - Android SDK
    - SDK Platform-tools
    - SDK Build-tools
    - Android Emulator
    - AVD Manager

#### Set Environment Variables

**For macOS/Linux:**
```bash
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

**For Windows:**
```
ANDROID_HOME = C:\Users\<username>\AppData\Local\Android\Sdk
Add to PATH:
%ANDROID_HOME%\emulator
%ANDROID_HOME%\platform-tools
%ANDROID_HOME%\tools
%ANDROID_HOME%\tools\bin
```

> ✅ Restart terminal or IDE after setting.

#### Create and Launch Android Emulator

List devices:
```bash
avdmanager list device
```

Create emulator:
```bash
avdmanager create avd -n Pixel_API_33 -k "system-images;android-33;google_apis;x86_64"
```

Launch emulator:
```bash
emulator -avd Pixel_API_33
```

> 📝 Make sure the emulator is running before executing the test.

---

### 3. Launch Appium Server (if Mobile)

```bash
appium
```

Or use Appium Desktop GUI and start server on port 4723.

---

### 4. Run Tests

```bash
mvn clean test -Dcucumber.filter.tags="@web"
```

---

### 📄 HTML Report

After execution, a custom HTML report is generated at:

```
/output/<project>_<timestamp>/main_report.html
```

- Includes test case details
- Screenshots for pass/fail steps
- Summary with tag, feature, and status grouping

---

### 👤 Author

Deepan Fernandez  
Test Automation Architect
