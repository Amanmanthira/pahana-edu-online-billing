
# 🧾 Pahana Institute - Smart Billing & Inventory Management System

Welcome to the official repository of the **Pahana Institute's Smart Billing & Inventory Dashboard** — a modern, responsive, and efficient full-stack web application built to manage customers, inventory, and billing in an educational institution.

> 🚀 Designed with simplicity in mind, but powerful enough to streamline admin tasks with real-time insights and billing automation.

---

## ✨ Features Overview

> A complete system to track, bill, and analyze institute operations — all from one elegant dashboard.

### 🧠 Core Functionalities:

- ✅ **Customer Management** – Add, edit, and track registered customers.
- ✅ **Item Management** – Manage inventory of all items sold or billed.
- ✅ **Billing System** – Generate bills instantly and store each transaction in the database.
- ✅ **Bill History Viewer** – Browse previously generated bills by account number or date.
- ✅ **Admin Dashboard Analytics** – Displays live stats:
  - 👥 Total Customers
  - 📦 Total Items
  - 💵 Total Billing Amount (in LKR)
- ✅ **Logout Functionality** – Secure session handling for admins.
- 🖨️ **Printable Bill Receipts** 

---

## 💻 Technology Stack

| Layer         | Technology         |
|---------------|--------------------|
| Frontend      | HTML, Inline CSS, JavaScript |
| Backend       | Java Servlet (Jakarta EE) |
| Database      | MySQL              |
| Build Tool    | Apache Maven       |
| Architecture  | MVC-S (Model-View-Controller-Service) |

---

## 📸 UI Snapshots

> *(Add screenshots after deployment to showcase your dashboard)*

---

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
- Apache Tomcat or Glassfish  
- MySQL Server  
- Maven (for building)  
- NetBeans / Eclipse IDE  

---

### 🗃️ MySQL Database Tables Required:

```sql
CREATE TABLE customers (
  account_no VARCHAR(50) PRIMARY KEY,
  name VARCHAR(100)
);

CREATE TABLE items (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  price DOUBLE
);

CREATE TABLE tdb (
  id INT PRIMARY KEY AUTO_INCREMENT,
  account_no VARCHAR(50),
  item_id INT,
  quantity INT,
  unit_price DOUBLE,
  subtotal DOUBLE,
  total DOUBLE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
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
