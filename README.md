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
```bash
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

### 3. Launch Appium Server (if Mobile)

```bash
appium
```
Or use Appium Desktop GUI and start server on port 4723.

### 4. Run Tests
```bash
mvn clean test -Dcucumber.filter.tags="@web"
```

### 📄 HTML Report<br>
After execution, a custom HTML report is generated at:
```bash
/output/<project>_<timestamp>/main_report.html
```
- Includes test case details
- Screenshots for pass/fail steps
- Summary with tag, feature, and status grouping

### 👤 Author
Deepan Fernandez<br>
Test Automation Architect
