# 🚀 Pahana EDU - Smart Billing System

A **full-stack Java Servlet** billing and inventory management system featuring real-time analytics and role-based dashboards for efficient business operations.

---

## ✨ Features

### 🔐 Authentication
- Role-based login system with distinct access for **Admin**, **Stock Keeper**, and **Cashier**

### 📊 Dashboard Analytics
- Overview of **Total Customers**, **Total Items**, and **Total Billing Amount**

### 🧾 Billing System
- Add items to bills seamlessly
- Print & save receipts for records
- View detailed billing history

### 📦 Inventory Management
- Add, edit, and delete inventory items effortlessly

### 🗣️ Team Chat
- Real-time chat functionality between Admin and Stock Keepers
- 
[https://github.com/Amanmanthira/pahana-edu-online-billing-with-automation/issues/1#issue-3234315051](https://github.com/user-attachments/assets/b55de90b-2166-4d96-97d4-e44333d3f464)

### 👤 Customer Management
- Add customers before billing 

---

## 👥 User Roles & Permissions

| Role          | Permissions                                             |
| ------------- | -------------------------------------------------------|
| **👨‍💼Admin**     | Analytics, Manage Items, Billing, Bill History, Bank History & amount , Users, Chat, Logout |
| **👨🏻‍💻Stock Keeper** | Analytics, Manage Items, Chat, Logout                 |
| **💵Cashier**   | Analytics, Add Customer, Billing, Bill History, Help, Logout  |

---

## 🛠️ Technology Stack

| Layer         | Technology                  |
| ------------- | --------------------------- |
| **Frontend**  | HTML, CSS, JavaScript       |
| **Backend**   | Java Servlet (Jakarta EE)   |
| **Database**  | MySQL                      |
| **Build**     | Maven                      |
| **Architecture** | MVC-S                   |

---

## 📸 UI Snapshots
---
https://github.com/user-attachments/assets/1b54f7f2-1444-42ad-a812-a2e76c23901b
## 
https://github.com/user-attachments/assets/9dfd4c49-308c-412d-a589-03640d5e9115
##
https://github.com/user-attachments/assets/577bb090-d74e-44aa-8960-f5b72c8952d4

## 📁 Project Structure

```
📦 src/
 ┣ 📂 com.pahana.controller/
 ┃ ┗ 📄 BillServlet.java
 ┣ 📂 com.pahana.dao/
 ┃ ┣ 📄 CustomerDAO.java
 ┃ ┗ 📄 ItemDAO.java
 ┣ 📂 com.pahana.model/
 ┃ ┣ 📄 Customer.java
 ┃ ┗ 📄 Item.java

📂 web/
 ┣ 📄 dashboard.html       # Main Admin Panel
 ┗ 📄 style.css (optional) # Custom styling (if separated)
```

---

## ⚙️ How It Works

### 🔄 Billing Process Flow

1. Admin selects a customer account number and items.
2. Each selected item is added with quantity and price.
3. The backend calculates subtotal and total bill.
4. Data is saved into the `tdb` table with timestamps.
5. The bill is displayed instantly and also stored permanently.
6. Admin can later visit the **Bill History** section to see all previous bills.

### 📊 Admin Dashboard Analytics

The `BillServlet`'s GET method handles admin analytics:

- `SELECT COUNT(*) FROM customers`  
- `SELECT COUNT(*) FROM items`  
- `SELECT SUM(total) FROM tdb`  

---

## 🛠️ Setup Instructions

### ✅ Prerequisites:

- Java 8+  
- Apache Tomcat or wildfly
- MySQL Server  
- Maven (for building)  
- NetBeans / Eclipse IDE  

---

### 🗃️ MySQL Database Tables Required:

```sql
CREATE TABLE IF NOT EXISTS `customers` (
  `account_no` varchar(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `address` text,
  `phone` varchar(20) DEFAULT NULL,
  `units` int(11) DEFAULT NULL,
  PRIMARY KEY (`account_no`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `items`;
CREATE TABLE IF NOT EXISTS `items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `price_per_unit` decimal(10,2) DEFAULT NULL,
  `quantity` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `tdb`;
CREATE TABLE IF NOT EXISTS `tdb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_no` varchar(50) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `team_chat`;
CREATE TABLE IF NOT EXISTS `team_chat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `role` varchar(30) DEFAULT NULL,
  `message` text,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `userRole` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

---

## 📦 How to Run the App

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/pahana-dashboard.git
```

### 2. Setup Project
- Import the project into **NetBeans** or **Eclipse**
- Update database credentials in `BillServlet.java`
- Ensure MySQL is running and tables are created

### 3. Build and Deploy
- Build the project using **Maven**
- Deploy the `.war` file or project to **Apache Tomcat**
- Access via: `http://localhost:8080/your-app/dashboard.html`

---

## 🧾 Bill History Example Output (API Response)

```json
[
  {
    "accountNo": "CUST001",
    "total": 2200,
    "createdAt": "2025-06-26 15:20:00"
  },
  {
    "accountNo": "CUST002",
    "total": 3400,
    "createdAt": "2025-06-26 16:00:00"
  }
]
```

---

## 👨‍💻 Author

**Aman Manthira**  
📍 *Gampaha, Sri Lanka*  
🎓 *Third-Year BSc Software Engineering Student @ ICBT*

---

### 💼 Professional Experience

- 🧠 **Founder** — *Amectar Softwares*  
- 💻 **Intern Software Engineer** — *AppsTechnologies*  
- 🌐 **Intern Full Stack Developer** — *Lakion*

---


## 💻 Tech Passion

- 🛠 MERN Stack Development
- ☕ Core Java & Servlet-based systems
- 🧪 Quality Assurance & Load Testing
- 🧠 Real-world System Building

---

## 🔗 Connect with Me

- 🌐 [LinkedIn](https://www.linkedin.com/in/aman-manthira-335a57268/)
- ✍️ [Dev.to](https://dev.to/amanmanthira)

---


## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

> 🔥 **Thank you for checking out the Pahana Billing Dashboard!** Contributions, stars ⭐, and forks 🍴 are warmly welcome.
