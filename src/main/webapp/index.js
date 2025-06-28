 const baseURL = 'http://localhost:8080/mavenproject2-1.0-SNAPSHOT';


document.addEventListener("DOMContentLoaded", () => {
  const textElement = document.getElementById("typed-text");
  const text = "Pahana Edu";
  let index = 0;
  let isDeleting = false;
  const typingSpeed = 180;
  const deletingSpeed = 100;
  const pauseTime = 1200;

  function type() {
    if (textElement) {
      if (isDeleting) {
        textElement.textContent = text.substring(0, index--);
      } else {
        textElement.textContent = text.substring(0, index++);
      }

      if (!isDeleting && index === text.length + 1) {
        isDeleting = true;
        setTimeout(type, pauseTime);
      } else if (isDeleting && index === 0) {
        isDeleting = false;
        setTimeout(type, pauseTime / 2);
      } else {
        setTimeout(type, isDeleting ? deletingSpeed : typingSpeed);
      }
    }
  }

  type();
});


  function showSection(section, el) {
    document.querySelectorAll('.section').forEach(s => s.style.display = 'none');
    document.getElementById(section).style.display = 'block';

    document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
    el.classList.add('active');

    if (section === 'customers') loadCustomers();
    if (section === 'items') loadItems();
    if (section === 'bill') clearBillForm();
  }

  function logout() {
    // Just redirect to login page without token logic
    location.href = 'login.html';
  }

  // --- Customers ---
  async function loadCustomers() {
    try {
      const res = await fetch(baseURL + '/CustomerServlet');
      if (!res.ok) throw new Error('Failed to fetch customers');
      const customers = await res.json();
      const tbody = document.querySelector('#customerTable tbody');
      tbody.innerHTML = '';
      customers.forEach(c => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${c.accountNo}</td>
          <td>${c.name}</td>
          <td>${c.address}</td>
          <td>${c.phone}</td>
          <td>${c.units}</td>
          <td>
            <button onclick="editCustomer('${c.accountNo}')">Edit</button>
            <button onclick="deleteCustomer('${c.accountNo}')">Delete</button>
          </td>
        `;
        tbody.appendChild(tr);
      });
      clearCustomerForm();
    } catch (err) {
      alert(err.message);
    }
  }

  async function addOrUpdateCustomer(e) {
    e.preventDefault();
    const accountNo = document.getElementById('customerAccountNoInput').value.trim();
    const name = document.getElementById('customerName').value.trim();
    const address = document.getElementById('customerAddress').value.trim();
    const phone = document.getElementById('customerPhone').value.trim();
    const units = parseInt(document.getElementById('customerUnits').value.trim());

    if (!accountNo || !name || !address || !phone || isNaN(units)) {
      alert('Please fill all fields');
      return;
    }

    const customerData = `accountNo=${encodeURIComponent(accountNo)}&name=${encodeURIComponent(name)}&address=${encodeURIComponent(address)}&phone=${encodeURIComponent(phone)}&units=${units}`;

    const isEditing = document.getElementById('customerAccountNo').value !== '';

    try {
      const res = await fetch(baseURL + '/CustomerServlet' + (isEditing ? `?accountNo=${encodeURIComponent(accountNo)}` : ''), {
        method: isEditing ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: customerData
      });

      if (!res.ok) throw new Error(await res.text());
      alert(isEditing ? 'Customer updated' : 'Customer added');
      loadCustomers();
    } catch (err) {
      alert('Error: ' + err.message);
    }
  }

  async function editCustomer(accountNo) {
    try {
      const res = await fetch(baseURL + `/CustomerServlet?accountNo=${encodeURIComponent(accountNo)}`);
      if (!res.ok) throw new Error('Customer not found');
      const c = await res.json();
      document.getElementById('customerAccountNoInput').value = c.accountNo;
      document.getElementById('customerAccountNo').value = c.accountNo; // hidden for editing
      document.getElementById('customerName').value = c.name;
      document.getElementById('customerAddress').value = c.address;
      document.getElementById('customerPhone').value = c.phone;
      document.getElementById('customerUnits').value = c.units;
    } catch (err) {
      alert(err.message);
    }
  }

  async function deleteCustomer(accountNo) {
    if (!confirm('Delete customer ' + accountNo + '?')) return;
    try {
      const res = await fetch(baseURL + `/CustomerServlet?accountNo=${encodeURIComponent(accountNo)}`, {
        method: 'DELETE'
      });
      if (!res.ok) throw new Error('Failed to delete');
      alert('Customer deleted');
      loadCustomers();
    } catch (err) {
      alert(err.message);
    }
  }

  function clearCustomerForm() {
    document.getElementById('customerAccountNoInput').value = '';
    document.getElementById('customerAccountNo').value = '';
    document.getElementById('customerName').value = '';
    document.getElementById('customerAddress').value = '';
    document.getElementById('customerPhone').value = '';
    document.getElementById('customerUnits').value = '';
  }
function clearItemForm() {
  document.getElementById('itemId').value = '';
  document.getElementById('itemName').value = '';
  document.getElementById('itemPrice').value = '';
}

  // --- Items ---
  async function loadItems() {
    try {
      const res = await fetch(baseURL + '/ItemServlet');
      if (!res.ok) throw new Error('Failed to fetch items');
      const items = await res.json();

      const tbody = document.querySelector('#itemTable tbody');
      tbody.innerHTML = '';
      items.forEach(i => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${i.id}</td>
          <td>${i.name}</td>
          <td>${i.price.toFixed(2)}</td>
          <td>
            <button onclick="editItem(${i.id})">Edit</button>
            <button onclick="deleteItem(${i.id})">Delete</button>
          </td>
        `;
        tbody.appendChild(tr);
      });
      clearItemForm();
    } catch (err) {
      alert(err.message);
    }
  }

  async function addOrUpdateItem(e) {
    e.preventDefault();
    const id = document.getElementById('itemId').value;
    const name = document.getElementById('itemName').value.trim();
    const price = parseFloat(document.getElementById('itemPrice').value);

    if (!name || isNaN(price)) {
      alert('Please fill all fields');
      return;
    }

    const itemData = `name=${encodeURIComponent(name)}&price=${price}`;

    try {
      const res = await fetch(baseURL + '/ItemServlet' + (id ? `?id=${id}` : ''), {
        method: id ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: itemData
      });
      if (!res.ok) throw new Error(await res.text());
      alert(id ? 'Item updated' : 'Item added');
      loadItems();
    } catch (err) {
      alert('Error: ' + err.message);
    }
  }

  async function editItem(id) {
  try {
    const res = await fetch(baseURL + `/ItemServlet?id=${id}`);
    if (!res.ok) throw new Error('Item not found');
    const data = await res.json();

    if (data.length === 0) throw new Error('No item returned');
    const i = data[0]; // Get the first item in the array

    document.getElementById('itemId').value = i.id;
    document.getElementById('itemName').value = i.name;
    document.getElementById('itemPrice').value = i.price;
  } catch (err) {
    alert(err.message);
  }
}


  async function deleteItem(id) {
    if (!confirm('Delete item ' + id + '?')) return;
    try {
      const res = await fetch(baseURL + `/ItemServlet?id=${id}`, {
        method: 'DELETE'
      });
      if (!res.ok) throw new Error('Failed to delete');
      alert('Item deleted');
      loadItems();
    } catch (err) {
      alert(err.message);
    }
  }

  // --- Bill ---
 function clearBillForm() {
  document.getElementById('billCustomerSelect').value = '';
  document.getElementById('billItemSelect').value = '';
  document.getElementById('billResult').innerHTML = '';
}

let billItems = []; // array to hold {id, name, price, quantity}

async function populateBillDropdowns() {
  try {
    // Load customers
    const cRes = await fetch(baseURL + '/CustomerServlet');
    const customers = await cRes.json();
    const customerSelect = document.getElementById('billCustomerSelect');
    customerSelect.innerHTML = '<option value="">Select Customer</option>';
    customers.forEach(c => {
      const opt = document.createElement('option');
      opt.value = c.accountNo;
      opt.textContent = `${c.name} (${c.accountNo})`;
      customerSelect.appendChild(opt);
    });

    // Load items
    const iRes = await fetch(baseURL + '/ItemServlet');
    const items = await iRes.json();
    const itemSelect = document.getElementById('billItemSelect');
    itemSelect.innerHTML = '';
    items.forEach(i => {
      const opt = document.createElement('option');
      opt.value = i.id;
      opt.textContent = `${i.name} - $${i.price.toFixed(2)}`;
      itemSelect.appendChild(opt);
    });

    billItems = [];
    renderBillItemsTable();
    updateBillTotal();
  } catch (err) {
    alert('Error loading dropdowns: ' + err.message);
  }
}

function addBillItem() {
  const itemSelect = document.getElementById('billItemSelect');
  const quantityInput = document.getElementById('billItemQuantity');

  const itemId = itemSelect.value;
  const quantity = parseInt(quantityInput.value);

  if (!itemId) {
    alert('Please select an item');
    return;
  }
  if (isNaN(quantity) || quantity < 1) {
    alert('Quantity must be at least 1');
    return;
  }

  // Check if item already added, then update quantity
  const existingIndex = billItems.findIndex(i => i.id == itemId);
  if (existingIndex !== -1) {
    billItems[existingIndex].quantity += quantity;
  } else {
    const itemText = itemSelect.options[itemSelect.selectedIndex].text;
    // Parse price from option text
    // Assuming format: "ItemName - $Price"
    const priceMatch = itemText.match(/\$(\d+(\.\d+)?)/);
    const price = priceMatch ? parseFloat(priceMatch[1]) : 0;

    billItems.push({
      id: itemId,
      name: itemText.split(' - $')[0],
      price,
      quantity
    });
  }

  renderBillItemsTable();
  updateBillTotal();

  // Reset quantity input
  quantityInput.value = 1;
}

function renderBillItemsTable() {
  const tbody = document.querySelector('#billItemsTable tbody');
  tbody.innerHTML = '';
  billItems.forEach((item, index) => {
    const tr = document.createElement('tr');
    const subtotal = item.price * item.quantity;
    tr.innerHTML = `
      <td>${item.name}</td>
      <td>${item.quantity}</td>
      <td>$${item.price.toFixed(2)}</td>
      <td>$${subtotal.toFixed(2)}</td>
      <td><button type="button" onclick="removeBillItem(${index})">Remove</button></td>
    `;
    tbody.appendChild(tr);
  });
}

function removeBillItem(index) {
  billItems.splice(index, 1);
  renderBillItemsTable();
  updateBillTotal();
}

function updateBillTotal() {
  const total = billItems.reduce((sum, i) => sum + i.price * i.quantity, 0);
  document.getElementById('billTotal').textContent = total.toFixed(2);
}

async function generateBill(e) {
  e.preventDefault();
  const accountNo = document.getElementById('billCustomerSelect').value;

  if (!accountNo) {
    alert('Please select a customer');
    return;
  }
  if (billItems.length === 0) {
    alert('Please add at least one item to the bill');
    return;
  }

  try {
    // Prepare data - array of {itemId, quantity}
    const itemsData = billItems.map(i => ({ itemId: i.id, quantity: i.quantity }));

    const res = await fetch(baseURL + '/BillServlet', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        accountNo,
        items: itemsData
      })
    });

    if (!res.ok) throw new Error('Failed to generate bill');
    const data = await res.json();

    // Show summary - you can customize based on your backend response
    document.getElementById('billResult').innerHTML = `
      <p><strong>Customer:</strong> ${data.name}</p>
      <p><strong>Items:</strong></p>
      <ul>
        ${data.items.map(it => `<li>${it.name} x ${it.quantity} = $${it.subtotal.toFixed(2)}</li>`).join('')}
      </ul>
      <p><strong>Total:</strong> $${data.total.toFixed(2)}</p>
    `;

    // Reset form
    billItems = [];
    renderBillItemsTable();
    updateBillTotal();
    clearBillForm();

  } catch (err) {
    alert(err.message);
  }
}
async function generateBill(e) {
  e.preventDefault();
  const accountNo = document.getElementById('billCustomerSelect').value;

  if (!accountNo) {
    alert('Please select a customer');
    return;
  }

  if (billItems.length === 0) {
    alert('Please add at least one item to the bill');
    return;
  }

  // Get customer name from dropdown text
  const customerName = document.querySelector(`#billCustomerSelect option[value="${accountNo}"]`)?.textContent || accountNo;

  // Calculate total and subtotals
  const itemsPreview = billItems.map(item => ({
    name: item.name,
    quantity: item.quantity,
    price: item.price,
    subtotal: item.price * item.quantity
  }));

  const total = itemsPreview.reduce((sum, item) => sum + item.subtotal, 0);

  // Fill the receipt UI
  document.getElementById('receiptCustomerInfo').innerHTML = `
    <p><strong>Name:</strong> ${customerName}</p>
    <p><strong>Account No:</strong> ${accountNo}</p>
  `;

  const receiptItems = document.getElementById('receiptItems');
  receiptItems.innerHTML = '';
  itemsPreview.forEach(item => {
    receiptItems.innerHTML += `
      <tr>
        <td>${item.name}</td>
        <td style="text-align:center;">${item.quantity}</td>
        <td style="text-align:right;">$${item.price.toFixed(2)}</td>
        <td style="text-align:right;">$${item.subtotal.toFixed(2)}</td>
      </tr>
    `;
  });

  document.getElementById('receiptTotal').textContent = total.toFixed(2);

  // Show the receipt and print button
  document.getElementById('printableReceipt').style.display = 'block';
  document.getElementById('btnPrintReceipt').style.display = 'inline-block';
}

document.addEventListener("DOMContentLoaded", () => {
  const textElement = document.getElementById("typed-text");
  const text = "Pahana Edu";
  let index = 0;
  let isDeleting = false;
  const typingSpeed = 180;
  const deletingSpeed = 100;
  const pauseTime = 1200;

  function type() {
    if (textElement) {
      if (isDeleting) {
        textElement.textContent = text.substring(0, index--);
      } else {
        textElement.textContent = text.substring(0, index++);
      }

      if (!isDeleting && index === text.length + 1) {
        isDeleting = true;
        setTimeout(type, pauseTime);
      } else if (isDeleting && index === 0) {
        isDeleting = false;
        setTimeout(type, pauseTime / 2);
      } else {
        setTimeout(type, isDeleting ? deletingSpeed : typingSpeed);
      }
    }
  }

  type();

  // ✅ Safe to add this now
  const printBtn = document.getElementById('btnPrintReceipt');
  if (printBtn) {
    printBtn.addEventListener('click', async () => {
      try {
        const accountNo = document.getElementById('billCustomerSelect').value;
        if (!accountNo) {
          alert('Select a customer first');
          return;
        }

        const itemsToSave = billItems.map(item => ({
          itemId: item.id,
          quantity: item.quantity
        }));

        if (itemsToSave.length === 0) {
          alert('Add items to the bill before printing');
          return;
        }

        const saveRes = await fetch(baseURL + '/BillServlet', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ accountNo, items: itemsToSave })
        });

        if (!saveRes.ok) throw new Error('Failed to save bill to database');

        downloadReceipt();
        alert('Bill saved and receipt downloaded!');
      } catch (err) {
        alert('Error: ' + err.message);
      }
    });
  }
});


async function downloadReceipt() {
  const receiptElement = document.getElementById('printableReceipt');
  const canvas = await html2canvas(receiptElement);
  const imgData = canvas.toDataURL('image/png');
    const { jsPDF } = window.jspdf;  

  const pdf = new jsPDF({
    orientation: 'portrait',
    unit: 'pt',
    format: [canvas.width, canvas.height]
  });

  pdf.addImage(imgData, 'PNG', 0, 0, canvas.width, canvas.height);
  pdf.save('receipt.pdf');
}




function showSection(section, el) {
  document.querySelectorAll('.section').forEach(s => s.style.display = 'none');

  const targetSection = document.getElementById(section);
  if (!targetSection) {
    console.error(`Section "${section}" not found in DOM.`);
    return;
  }

  targetSection.style.display = 'block';

  document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
  if (el) el.classList.add('active');

  if (section === 'customers') loadCustomers();
  if (section === 'items') loadItems();
  if (section === 'bill') {
    clearBillForm();
    populateBillDropdowns();
  }
  if (section === 'analytics') loadAnalytics();
  if (section === 'history') loadHistory();
}


async function loadAnalytics() {
  try {
    // Fetch data as before ...
    const customerRes = await fetch(baseURL + '/CustomerServlet');
    const customers = await customerRes.json();
    document.getElementById('totalCustomers').textContent = customers.length;

    const itemRes = await fetch(baseURL + '/ItemServlet');
    const items = await itemRes.json();
    document.getElementById('totalItems').textContent = items.length;

    const billRes = await fetch(baseURL + '/BillServlet?summary=true');
    const summary = await billRes.json();
    document.getElementById('totalBills').textContent = summary.totalBills;

    // Load products for slider
    const track = document.getElementById('productSliderTrack');
    track.innerHTML = '';

    items.forEach(item => {
      const card = document.createElement('div');
      card.style = `
        background: #fff;
        border-radius: 20px;
        box-shadow: 0 6px 20px rgba(0,0,0,0.1);
        width: 200px;
        flex-shrink: 0;
        text-align: center;
        padding: 20px 15px;
        font-family: 'Poppins', sans-serif;
        cursor: pointer;
        margin-right: 20px;
        transition: transform 0.3s ease;
      `;
      card.innerHTML = `
        <div style="width:100%; height:140px; background:#ffe6f0; border-radius:16px; margin-bottom:15px; display:flex; align-items:center; justify-content:center;">
          <i class="fas fa-box" style="font-size:50px; color:#e91e63;"></i>
        </div>
        <div style="font-size:18px; font-weight:600; color:#333;">${item.name}</div>
        <div style="font-size:16px; color:#888;">LKR ${item.price.toFixed(2)}</div>
      `;
      track.appendChild(card);
    });

    // Slider controls & auto-slide
    let index = 0;
    const slider = document.getElementById('productSlider');
    const cardWidth = 220; // card width + margin-right approx
    const visibleCards = Math.floor(slider.offsetWidth / cardWidth);
    const maxIndex = Math.max(0, items.length - visibleCards);

    function updateSlider() {
      track.style.transform = `translateX(-${index * cardWidth}px)`;
    }

    document.querySelector('#productSlider button.prev').onclick = () => {
      index = (index === 0) ? maxIndex : index - 1;
      updateSlider();
      resetAutoSlide();
    };

    document.querySelector('#productSlider button.next').onclick = () => {
      index = (index === maxIndex) ? 0 : index + 1;
      updateSlider();
      resetAutoSlide();
    };

    // Auto slide every 3 seconds
    let autoSlideInterval = setInterval(() => {
      index = (index === maxIndex) ? 0 : index + 1;
      updateSlider();
    }, 3000);

    function resetAutoSlide() {
      clearInterval(autoSlideInterval);
      autoSlideInterval = setInterval(() => {
        index = (index === maxIndex) ? 0 : index + 1;
        updateSlider();
      }, 3000);
    }

  } catch (err) {
    alert('Failed to load analytics: ' + err.message);
  }
}



async function loadHistory() {
  try {
    const res = await fetch(baseURL + '/BillServlet?history=true');
    const bills = await res.json();

    const tbody = document.getElementById('billHistoryBody');
    tbody.innerHTML = "";

    bills.forEach(bill => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td style="padding: 10px; border: 1px solid #ddd;">${bill.accountNo}</td>
        <td style="padding: 10px; border: 1px solid #ddd;">LKR ${bill.total.toFixed(2)}</td>
        <td style="padding: 10px; border: 1px solid #ddd;">${bill.date}</td>
      `;
      tbody.appendChild(row);
    });
  } catch (err) {
    alert("Failed to load bill history: " + err.message);
  }
}


  // Load customers on start
  showSection('customers', document.querySelector('.nav-item'));
