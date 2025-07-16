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
https://github.com/user-attachments/assets/7cc1e0ac-158c-4ce1-bd97-b957c17c009a


### 👤 Customer Management
- Add customers before billing (**Cashier only**)

---

## 👥 User Roles & Permissions

| Role          | Permissions                                             |
| ------------- | -------------------------------------------------------|
| **Admin**     | Analytics, Manage Items, Billing, Bill History, Users, Chat, Logout |
| **Stock Keeper** | Analytics, Manage Items, Chat, Logout                 |
| **Cashier**   | Analytics, Add Customer, Billing, Bill History, Help, Logout  |

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

![UI Preview](https://github.com/user-attachments/assets/64028d76-4354-4db1-89a6-3e3336e1ee85)


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
