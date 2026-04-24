#   
 import { useState, useEffect, useRef } from "react";  
  
const SUPABASE_URL = "https://qnzgtgfriwtvcbdmvnsc.supabase.co";  
const SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFuemd0Z2ZyaXd0dmNiZG12bnNjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzcwMjAxNjksImV4cCI6MjA5MjU5NjE2OX0.2PwVx8etZF6OTBEVmi5mpAXq5wbmNfSgO9s4UJf9Arc";  
const ADMIN_PASS = "Adith123";  
  
const db = {  
  insertOrder: async (o) => {  
    const res = await fetch(`${SUPABASE_URL}/rest/v1/orders`, {  
      method: "POST",  
      headers: { "Content-Type": "application/json", apikey: SUPABASE_KEY, Authorization: `Bearer ${SUPABASE_KEY}`, Prefer: "return=representation" },  
      body: JSON.stringify(o),  
    });  
    const json = await res.json();  
    return { data: json, error: res.ok ? null : json };  
  },  
  fetchOrders: async () => {  
    const res = await fetch(`${SUPABASE_URL}/rest/v1/orders?select=*&order=created_at.desc`, {  
      headers: { apikey: SUPABASE_KEY, Authorization: `Bearer ${SUPABASE_KEY}` },  
    });  
    const json = await res.json();  
    return { data: res.ok ? json : [], error: res.ok ? null : json };  
  },  
  updateOrder: async (id, patch) => {  
    const res = await fetch(`${SUPABASE_URL}/rest/v1/orders?id=eq.${id}`, {  
      method: "PATCH",  
      headers: { "Content-Type": "application/json", apikey: SUPABASE_KEY, Authorization: `Bearer ${SUPABASE_KEY}`, Prefer: "return=representation" },  
      body: JSON.stringify(patch),  
    });  
    const json = await res.json();  
    return { data: json, error: res.ok ? null : json };  
  },  
};  
  
// Unsplash images mapped to product names  
const IMG = {  
  // Fruits  
  "Banana": "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=300&q=80",  
  "Pineapple": "https://images.unsplash.com/photo-1550258987-190a2d41a8ba?w=300&q=80",  
  "Papaya": "https://images.unsplash.com/photo-1517282009859-f000ec3b26fe?w=300&q=80",  
  "Mango": "https://images.unsplash.com/photo-1553279768-865429fa0078?w=300&q=80",  
  "Jackfruit": "https://images.unsplash.com/photo-1590165482129-1b8b27698780?w=300&q=80",  
  "Guava": "https://images.unsplash.com/photo-1536511132770-e5058c7e8c46?w=300&q=80",  
  "Coconut": "https://images.unsplash.com/photo-1578020190125-f4f7c18bc9cb?w=300&q=80",  
  "Sapota": "https://images.unsplash.com/photo-1619566636858-adf3ef46400b?w=300&q=80",  
  "Custard Apple": "https://images.unsplash.com/photo-1602250013674-56e5e95db7a5?w=300&q=80",  
  "Watermelon": "https://images.unsplash.com/photo-1563114773-84221bd62daa?w=300&q=80",  
  "Orange": "https://images.unsplash.com/photo-1582979512210-99b6a53386f9?w=300&q=80",  
  "Grapes": "https://images.unsplash.com/photo-1423483641154-5411ec9c0ddf?w=300&q=80",  
  "Apple": "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=300&q=80",  
  "Pomegranate": "https://images.unsplash.com/photo-1615485290382-441e4d049cb5?w=300&q=80",  
  "Gooseberry": "https://images.unsplash.com/photo-1601004890684-d8cbf643f5f2?w=300&q=80",  
  // Vegetables  
  "Onion": "https://images.unsplash.com/photo-1580201092675-a0a6a6cafbb1?w=300&q=80",  
  "Potato": "https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=300&q=80",  
  "Tomato": "https://images.unsplash.com/photo-1546470427-227c4a67b5c4?w=300&q=80",  
  "Brinjal": "https://images.unsplash.com/photo-1659261200833-ec8761558af7?w=300&q=80",  
  "Okra": "https://images.unsplash.com/photo-1628773822503-930a7eaecf80?w=300&q=80",  
  "Green Chilli": "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=300&q=80",  
  "Ginger": "https://images.unsplash.com/photo-1615485500704-8e990f9900f7?w=300&q=80",  
  "Garlic": "https://images.unsplash.com/photo-1501420193828-9bc18d0e6c1b?w=300&q=80",  
  "Cabbage": "https://images.unsplash.com/photo-1594282486552-05b4d80fbb9f?w=300&q=80",  
  "Cauliflower": "https://images.unsplash.com/photo-1568584711271-6c929fb49b60?w=300&q=80",  
  "Carrot": "https://images.unsplash.com/photo-1445282768818-728615cc910a?w=300&q=80",  
  "Beetroot": "https://images.unsplash.com/photo-1593105544559-ecb03bf76f82?w=300&q=80",  
  "Beans": "https://images.unsplash.com/photo-1567375698348-5d9d5ae99de0?w=300&q=80",  
  "Cucumber": "https://images.unsplash.com/photo-1449300079323-02e209d9d3a6?w=300&q=80",  
  "Pumpkin": "https://images.unsplash.com/photo-1570586437263-ab629fccc818?w=300&q=80",  
  "Bitter Gourd": "https://images.unsplash.com/photo-1628773822503-930a7eaecf80?w=300&q=80",  
  "Snake Gourd": "https://images.unsplash.com/photo-1601493700631-2b16ec4b4716?w=300&q=80",  
  "Bottle Gourd": "https://images.unsplash.com/photo-1601493700631-2b16ec4b4716?w=300&q=80",  
  "Drumstick": "https://images.unsplash.com/photo-1601493700631-2b16ec4b4716?w=300&q=80",  
  "Spinach": "https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=300&q=80",  
  // Bakery  
  "Bread": "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=300&q=80",  
  "Bun": "https://images.unsplash.com/photo-1612198273689-b9b9ad72a5d4?w=300&q=80",  
  "Rusk": "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=300&q=80",  
  "Biscuit": "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=300&q=80",  
  "Cake": "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=300&q=80",  
  "Pastry": "https://images.unsplash.com/photo-1587314168485-3236d6710814?w=300&q=80",  
  "Muffin": "https://images.unsplash.com/photo-1607958996333-41aef7caefaa?w=300&q=80",  
  "Donut": "https://images.unsplash.com/photo-1551024601-bec78aea704b?w=300&q=80",  
  "Puffs": "https://images.unsplash.com/photo-1621743478914-cc8a86d7e7b5?w=300&q=80",  
  "Rolls": "https://images.unsplash.com/photo-1549931319-a545dcf3bc7b?w=300&q=80",  
  "Sandwich": "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=300&q=80",  
  "Cream Bun": "https://images.unsplash.com/photo-1612198273689-b9b9ad72a5d4?w=300&q=80",  
  "Plum Cake": "https://images.unsplash.com/photo-1562440499-64c9a111f713?w=300&q=80",  
  "Cookies": "https://images.unsplash.com/photo-1499636136210-6f4ee915583e?w=300&q=80",  
  "Brownie": "https://images.unsplash.com/photo-1515037893149-de7f840978e2?w=300&q=80",  
  // Snacks  
  "Chips": "https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=300&q=80",  
  "Banana Chips": "https://images.unsplash.com/photo-1604508915671-4c7f7c99c0fe?w=300&q=80",  
  "Mixture": "https://images.unsplash.com/photo-1599490659213-e2b9527bd087?w=300&q=80",  
  "Murukku": "https://images.unsplash.com/photo-1599490659213-e2b9527bd087?w=300&q=80",  
  "Achappam": "https://images.unsplash.com/photo-1599490659213-e2b9527bd087?w=300&q=80",  
  "Kuzhalappam": "https://images.unsplash.com/photo-1599490659213-e2b9527bd087?w=300&q=80",  
  "Popcorn": "https://images.unsplash.com/photo-1585647347483-22b66260dfff?w=300&q=80",  
  "Samosa": "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=300&q=80",  
  "Cutlet": "https://images.unsplash.com/photo-1606755962773-d324e0a13086?w=300&q=80",  
  "Vada": "https://images.unsplash.com/photo-1630383249896-483b1b59b680?w=300&q=80",  
  "Pakoda": "https://images.unsplash.com/photo-1599490659213-e2b9527bd087?w=300&q=80",  
  "Roasted Nuts": "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=300&q=80",  
  // Beverages  
  "Tea": "https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=300&q=80",  
  "Coffee": "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=300&q=80",  
  "Milk": "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=300&q=80",  
  "Buttermilk": "https://images.unsplash.com/photo-1570197788417-0e82375c9371?w=300&q=80",  
  "Lemon Juice": "https://images.unsplash.com/photo-1621506289937-a8e4df240d0b?w=300&q=80",  
  "Tender Coconut": "https://images.unsplash.com/photo-1555685812-4b943f1cb0eb?w=300&q=80",  
  "Fruit Juice": "https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=300&q=80",  
  "Soda": "https://images.unsplash.com/photo-1625772299848-391b6a87d7b3?w=300&q=80",  
  "Soft Drinks": "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=300&q=80",  
  "Energy Drinks": "https://images.unsplash.com/photo-1584105055010-c4abb6d70c3c?w=300&q=80",  
  "Malt Drinks": "https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=300&q=80",  
  "Lassi": "https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=300&q=80",  
  "Cold Coffee": "https://images.unsplash.com/photo-1461023058943-07fcbe16d735?w=300&q=80",  
  "Iced Tea": "https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=300&q=80",  
  "Flavoured Milk": "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=300&q=80",  
  // Meat & Fish  
  "Chicken": "https://images.unsplash.com/photo-1604503468506-a8da13d11bbc?w=300&q=80",  
  "Beef": "https://images.unsplash.com/photo-1558030006-450675393462?w=300&q=80",  
  "Mutton": "https://images.unsplash.com/photo-1602491453631-e2a5ad90a131?w=300&q=80",  
  "Pork": "https://images.unsplash.com/photo-1529692236671-f1f6cf9683ba?w=300&q=80",  
  "Duck": "https://images.unsplash.com/photo-1607116667981-ff40b4e6e6c1?w=300&q=80",  
  "Sardine": "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=300&q=80",  
  "Mackerel": "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=300&q=80",  
  "Tuna": "https://images.unsplash.com/photo-1556906781-9a412961a28c?w=300&q=80",  
  "Seer Fish": "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=300&q=80",  
  "Prawns": "https://images.unsplash.com/photo-1565680018434-b513d5e5fd47?w=300&q=80",  
  "Crab": "https://images.unsplash.com/photo-1559737558-2f5a35f4523b?w=300&q=80",  
  "Squid": "https://images.unsplash.com/photo-1559737558-2f5a35f4523b?w=300&q=80",  
  "Clams": "https://images.unsplash.com/photo-1559737558-2f5a35f4523b?w=300&q=80",  
  "Mussels": "https://images.unsplash.com/photo-1559737558-2f5a35f4523b?w=300&q=80",  
  "Dry Fish": "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=300&q=80",  
};  
  
const CATEGORIES = [  
  { id: "all", label: "All", icon: "✦" },  
  { id: "fruits", label: "Fruits", icon: "🍎" },  
  { id: "vegetables", label: "Vegetables", icon: "🥦" },  
  { id: "dairy", label: "Dairy & Eggs", icon: "🥛" },  
  { id: "bakery", label: "Bakery", icon: "🍞" },  
  { id: "meat", label: "Meat & Fish", icon: "🍗" },  
  { id: "snacks", label: "Snacks", icon: "🍟" },  
  { id: "beverages", label: "Beverages", icon: "🥤" },  
];  
  
const PRODUCTS = [  
  // FRUITS  
  { id: 1,  name:"Banana",        category:"fruits",     price:50,  unit:"per kg",    color:"#f9a825" },  
  { id: 2,  name:"Pineapple",     category:"fruits",     price:60,  unit:"per kg",    color:"#fbc02d" },  
  { id: 3,  name:"Papaya",        category:"fruits",     price:50,  unit:"per kg",    color:"#ef6c00" },  
  { id: 4,  name:"Mango",         category:"fruits",     price:120, unit:"per kg",    color:"#f57f17", badge:"Premium" },  
  { id: 5,  name:"Jackfruit",     category:"fruits",     price:100, unit:"per kg",    color:"#f9a825" },  
  { id: 6,  name:"Guava",         category:"fruits",     price:60,  unit:"per kg",    color:"#558b2f" },  
  { id: 7,  name:"Coconut",       category:"fruits",     price:90,  unit:"each",      color:"#6d4c41" },  
  { id: 8,  name:"Sapota",        category:"fruits",     price:70,  unit:"per kg",    color:"#8d6e63" },  
  { id: 9,  name:"Custard Apple", category:"fruits",     price:80,  unit:"per kg",    color:"#388e3c" },  
  { id: 10, name:"Watermelon",    category:"fruits",     price:30,  unit:"per kg",    color:"#c62828" },  
  { id: 11, name:"Orange",        category:"fruits",     price:100, unit:"per kg",    color:"#e64a19" },  
  { id: 12, name:"Grapes",        category:"fruits",     price:120, unit:"per kg",    color:"#6a1b9a" },  
  { id: 13, name:"Apple",         category:"fruits",     price:200, unit:"per kg",    color:"#c62828", badge:"Premium" },  
  { id: 14, name:"Pomegranate",   category:"fruits",     price:150, unit:"per kg",    color:"#ad1457" },  
  { id: 15, name:"Gooseberry",    category:"fruits",     price:130, unit:"per kg",    color:"#558b2f" },  
  // VEGETABLES  
  { id: 16, name:"Onion",         category:"vegetables", price:40,  unit:"per kg",    color:"#ad1457" },  
  { id: 17, name:"Potato",        category:"vegetables", price:30,  unit:"per kg",    color:"#8d6e63" },  
  { id: 18, name:"Tomato",        category:"vegetables", price:25,  unit:"per kg",    color:"#c62828", badge:"Fresh" },  
  { id: 19, name:"Brinjal",       category:"vegetables", price:45,  unit:"per kg",    color:"#6a1b9a" },  
  { id: 20, name:"Okra",          category:"vegetables", price:50,  unit:"per kg",    color:"#388e3c" },  
  { id: 21, name:"Green Chilli",  category:"vegetables", price:80,  unit:"per kg",    color:"#2e7d32" },  
  { id: 22, name:"Ginger",        category:"vegetables", price:110, unit:"per kg",    color:"#f9a825" },  
  { id: 23, name:"Garlic",        category:"vegetables", price:180, unit:"per kg",    color:"#f0ede8" },  
  { id: 24, name:"Cabbage",       category:"vegetables", price:30,  unit:"per kg",    color:"#558b2f" },  
  { id: 25, name:"Cauliflower",   category:"vegetables", price:45,  unit:"per kg",    color:"#f5f5f5" },  
  { id: 26, name:"Carrot",        category:"vegetables", price:40,  unit:"per kg",    color:"#e64a19" },  
  { id: 27, name:"Beetroot",      category:"vegetables", price:40,  unit:"per kg",    color:"#880e4f" },  
  { id: 28, name:"Beans",         category:"vegetables", price:80,  unit:"per kg",    color:"#388e3c" },  
  { id: 29, name:"Cucumber",      category:"vegetables", price:35,  unit:"per kg",    color:"#2e7d32" },  
  { id: 30, name:"Pumpkin",       category:"vegetables", price:30,  unit:"per kg",    color:"#e64a19" },  
  { id: 31, name:"Bitter Gourd",  category:"vegetables", price:50,  unit:"per kg",    color:"#388e3c" },  
  { id: 32, name:"Snake Gourd",   category:"vegetables", price:45,  unit:"per kg",    color:"#2e7d32" },  
  { id: 33, name:"Bottle Gourd",  category:"vegetables", price:35,  unit:"per kg",    color:"#558b2f" },  
  { id: 34, name:"Drumstick",     category:"vegetables", price:60,  unit:"per kg",    color:"#558b2f" },  
  { id: 35, name:"Spinach",       category:"vegetables", price:15,  unit:"per bunch", color:"#1b5e20", badge:"Organic" },  
  // BAKERY  
  { id: 36, name:"Bread",         category:"bakery",     price:50,  unit:"per loaf",  color:"#8d6e63" },  
  { id: 37, name:"Bun",           category:"bakery",     price:8,   unit:"each",      color:"#a1887f" },  
  { id: 38, name:"Rusk",          category:"bakery",     price:100, unit:"per pack",  color:"#bcaaa4" },  
  { id: 39, name:"Biscuit",       category:"bakery",     price:30,  unit:"per pack",  color:"#d7ccc8" },  
  { id: 40, name:"Cake",          category:"bakery",     price:500, unit:"per kg",    color:"#f06292", badge:"Premium" },  
  { id: 41, name:"Pastry",        category:"bakery",     price:80,  unit:"each",      color:"#f48fb1" },  
  { id: 42, name:"Muffin",        category:"bakery",     price:50,  unit:"each",      color:"#ce93d8" },  
  { id: 43, name:"Donut",         category:"bakery",     price:70,  unit:"each",      color:"#ffcc80" },  
  { id: 44, name:"Puffs",         category:"bakery",     price:30,  unit:"each",      color:"#ffe082" },  
  { id: 45, name:"Rolls",         category:"bakery",     price:60,  unit:"each",      color:"#bcaaa4" },  
  { id: 46, name:"Sandwich",      category:"bakery",     price:80,  unit:"each",      color:"#a5d6a7" },  
  { id: 47, name:"Cream Bun",     category:"bakery",     price:30,  unit:"each",      color:"#ffe0b2" },  
  { id: 48, name:"Plum Cake",     category:"bakery",     price:600, unit:"per kg",    color:"#8d6e63", badge:"Premium" },  
  { id: 49, name:"Cookies",       category:"bakery",     price:250, unit:"per kg",    color:"#d7ccc8" },  
  { id: 50, name:"Brownie",       category:"bakery",     price:120, unit:"each",      color:"#4e342e" },  
  // SNACKS  
  { id: 51, name:"Chips",         category:"snacks",     price:30,  unit:"per pack",  color:"#f9a825" },  
  { id: 52, name:"Banana Chips",  category:"snacks",     price:250, unit:"per kg",    color:"#fbc02d" },  
  { id: 53, name:"Mixture",       category:"snacks",     price:200, unit:"per kg",    color:"#ef6c00" },  
  { id: 54, name:"Murukku",       category:"snacks",     price:200, unit:"per kg",    color:"#e65100" },  
  { id: 55, name:"Achappam",      category:"snacks",     price:250, unit:"per kg",    color:"#d7ccc8" },  
  { id: 56, name:"Kuzhalappam",   category:"snacks",     price:250, unit:"per kg",    color:"#bcaaa4" },  
  { id: 57, name:"Popcorn",       category:"snacks",     price:30,  unit:"per pack",  color:"#ffe082" },  
  { id: 58, name:"Samosa",        category:"snacks",     price:20,  unit:"each",      color:"#f9a825" },  
  { id: 59, name:"Cutlet",        category:"snacks",     price:20,  unit:"each",      color:"#8d6e63" },  
  { id: 60, name:"Vada",          category:"snacks",     price:15,  unit:"each",      color:"#a1887f" },  
  { id: 61, name:"Pakoda",        category:"snacks",     price:30,  unit:"per plate", color:"#ef6c00" },  
  { id: 62, name:"Roasted Nuts",  category:"snacks",     price:400, unit:"per kg",    color:"#8d6e63", badge:"Premium" },  
  // BEVERAGES  
  { id: 63, name:"Tea",           category:"beverages",  price:15,  unit:"per cup",   color:"#8d6e63" },  
  { id: 64, name:"Coffee",        category:"beverages",  price:20,  unit:"per cup",   color:"#4e342e" },  
  { id: 65, name:"Milk",          category:"beverages",  price:60,  unit:"per litre", color:"#e3f2fd" },  
  { id: 66, name:"Buttermilk",    category:"beverages",  price:25,  unit:"per glass", color:"#fff9c4" },  
  { id: 67, name:"Lemon Juice",   category:"beverages",  price:30,  unit:"per glass", color:"#f9a825" },  
  { id: 68, name:"Tender Coconut",category:"beverages",  price:50,  unit:"each",      color:"#a5d6a7" },  
  { id: 69, name:"Fruit Juice",   category:"beverages",  price:50,  unit:"per glass", color:"#ef6c00" },  
  { id: 70, name:"Soda",          category:"beverages",  price:30,  unit:"per bottle",color:"#b3e5fc" },  
  { id: 71, name:"Soft Drinks",   category:"beverages",  price:60,  unit:"per bottle",color:"#b3e5fc" },  
  { id: 72, name:"Energy Drinks", category:"beverages",  price:120, unit:"per can",   color:"#76ff03" },  
  { id: 73, name:"Malt Drinks",   category:"beverages",  price:250, unit:"per bottle",color:"#ffe082" },  
  { id: 74, name:"Lassi",         category:"beverages",  price:60,  unit:"per glass", color:"#fff9c4" },  
  { id: 75, name:"Cold Coffee",   category:"beverages",  price:80,  unit:"per glass", color:"#4e342e", badge:"Craft" },  
  { id: 76, name:"Iced Tea",      category:"beverages",  price:70,  unit:"per glass", color:"#a5d6a7" },  
  { id: 77, name:"Flavoured Milk",category:"beverages",  price:40,  unit:"per bottle",color:"#f8bbd0" },  
  // MEAT & FISH  
  { id: 78, name:"Chicken",       category:"meat",       price:220, unit:"per kg",    color:"#f9a825", badge:"Fresh" },  
  { id: 79, name:"Beef",          category:"meat",       price:320, unit:"per kg",    color:"#c62828" },  
  { id: 80, name:"Mutton",        category:"meat",       price:800, unit:"per kg",    color:"#8d6e63", badge:"Premium" },  
  { id: 81, name:"Pork",          category:"meat",       price:300, unit:"per kg",    color:"#e64a19" },  
  { id: 82, name:"Duck",          category:"meat",       price:500, unit:"per kg",    color:"#8d6e63" },  
  { id: 83, name:"Sardine",       category:"meat",       price:150, unit:"per kg",    color:"#1565c0" },  
  { id: 84, name:"Mackerel",      category:"meat",       price:200, unit:"per kg",    color:"#0277bd" },  
  { id: 85, name:"Tuna",          category:"meat",       price:280, unit:"per kg",    color:"#1565c0" },  
  { id: 86, name:"Seer Fish",     category:"meat",       price:800, unit:"per kg",    color:"#0d47a1", badge:"Premium" },  
  { id: 87, name:"Prawns",        category:"meat",       price:450, unit:"per kg",    color:"#e64a19" },  
  { id: 88, name:"Crab",          category:"meat",       price:400, unit:"per kg",    color:"#c62828" },  
  { id: 89, name:"Squid",         category:"meat",       price:300, unit:"per kg",    color:"#7b1fa2" },  
  { id: 90, name:"Clams",         category:"meat",       price:120, unit:"per kg",    color:"#8d6e63" },  
  { id: 91, name:"Mussels",       category:"meat",       price:150, unit:"per kg",    color:"#4e342e" },  
  { id: 92, name:"Dry Fish",      category:"meat",       price:500, unit:"per kg",    color:"#8d6e63" },  
];  
  
const OFFERS = [  
  { title: "Weekend Special", desc: "20% off all Organic items", color: "#d4a762", bg: "linear-gradient(135deg,#1a1a1a,#2d2010)" },  
  { title: "Fresh Arrival", desc: "New season mangoes in stock", color: "#62d4a7", bg: "linear-gradient(135deg,#0d1f1a,#0a2a20)" },  
  { title: "Bulk Discount", desc: "Buy 3 get 1 free on dairy", color: "#a762d4", bg: "linear-gradient(135deg,#1a0d1f,#200a2a)" },  
];  
  
const STATUS = {  
  pending:   { bg: "#2a1f00", color: "#d4a762", label: "Pending" },  
  confirmed: { bg: "#0a1f2a", color: "#62b4d4", label: "Confirmed" },  
  delivered: { bg: "#0a2a14", color: "#62d4a7", label: "Delivered" },  
  cancelled: { bg: "#2a0a0a", color: "#d46262", label: "Cancelled" },  
};  
  
export default function App() {  
  const [cat, setCat] = useState("all");  
  const [cart, setCart] = useState({});  
  const [search, setSearch] = useState("");  
  const [page, setPage] = useState("home");  
  const [offerIdx, setOfferIdx] = useState(0);  
  const [notif, setNotif] = useState(null);  
  const [notifType, setNotifType] = useState("ok");  
  const [wishlist, setWishlist] = useState(new Set());  
  const [sort, setSort] = useState("default");  
  const [orders, setOrders] = useState([]);  
  const [ordersLoading, setOrdersLoading] = useState(false);  
  const [name, setName] = useState("");  
  const [email, setEmail] = useState("");  
  const [phone, setPhone] = useState("");  
  const [addr, setAddr] = useState("");  
  const [placing, setPlacing] = useState(false);  
  const [checkout, setCheckout] = useState(false);  
  // Admin auth  
  const [adminAuthed, setAdminAuthed] = useState(false);  
  const [adminPass, setAdminPass] = useState("");  
  const [adminErr, setAdminErr] = useState("");  
  
  useEffect(() => {  
    const t = setInterval(() => setOfferIdx(i => (i + 1) % OFFERS.length), 3500);  
    return () => clearInterval(t);  
  }, []);  
  
  const toast = (msg, type = "ok") => {  
    setNotif(msg); setNotifType(type);  
    setTimeout(() => setNotif(null), 2500);  
  };  
  
  const loadOrders = async () => {  
    setOrdersLoading(true);  
    const { data, error } = await db.fetchOrders();  
    if (error) toast("Could not load orders", "err");  
    else setOrders(data);  
    setOrdersLoading(false);  
  };  
  
  useEffect(() => { if (page === "admin" && adminAuthed) loadOrders(); }, [page, adminAuthed]);  
  
  const addToCart = (p) => { setCart(c => ({ ...c, [p.id]: (c[p.id] || 0) + 1 })); toast(`${p.name} added`); };  
  const dec = (id) => setCart(c => { const n = { ...c }; n[id] > 1 ? n[id]-- : delete n[id]; return n; });  
  const toggleWish = (id) => setWishlist(w => { const n = new Set(w); n.has(id) ? n.delete(id) : n.add(id); return n; });  
  
  const cartCount = Object.values(cart).reduce((a, b) => a + b, 0);  
  const cartTotal = Object.entries(cart).reduce((s, [id, qty]) => s + (PRODUCTS.find(p => p.id === +id)?.price || 0) * qty, 0);  
  const cartItems = Object.entries(cart).map(([id, qty]) => ({ ...PRODUCTS.find(p => p.id === +id), qty }));  
  
  const placeOrder = async () => {  
    if (!name.trim()) { toast("Enter your name", "err"); return; }  
    if (!email.trim()) { toast("Enter your email", "err"); return; }  
    setPlacing(true);  
    const items = cartItems.map(i => ({ id: i.id, name: i.name, qty: i.qty, price: i.price }));  
    const { error } = await db.insertOrder({ customer_name: name, customer_email: email, items, total: cartTotal, status: "pending" });  
    setPlacing(false);  
    if (error) { toast("Order failed. Try again.", "err"); return; }  
    setCart({}); setName(""); setEmail(""); setPhone(""); setAddr(""); setCheckout(false);  
    toast("Order placed! 🎉"); setPage("home");  
  };  
  
  const filtered = PRODUCTS.filter(p =>  
    (cat === "all" || p.category === cat) &&  
    p.name.toLowerCase().includes(search.toLowerCase())  
  ).sort((a, b) =>  
    sort === "price-asc" ? a.price - b.price :  
    sort === "price-desc" ? b.price - a.price : 0  
  );  
  
  const goAdmin = () => { setPage("admin"); setCheckout(false); };  
  
  return (  
    <div style={S.root}>  
      <style>{`  
        @keyframes spin{to{transform:rotate(360deg)}}  
        @keyframes slideDown{from{opacity:0;transform:translateX(-50%) translateY(-12px)}to{opacity:1;transform:translateX(-50%) translateY(0)}}  
        *{box-sizing:border-box}  
        img{display:block}  
        ::-webkit-scrollbar{display:none}  
      `}</style>  
  
      {notif && (  
        <div style={{ ...S.notif, background: notifType === "err" ? "#b71c1c" : "#d4a762", color: notifType === "err" ? "#fff" : "#0a0a0a", animation: "slideDown 0.25s ease" }}>  
          {notifType === "err" ? "✕  " : "✓  "}{notif}  
        </div>  
      )}  
  
      {/* HEADER */}  
      <header style={S.header}>  
        <div>  
          <div style={S.logo}><span style={{ color: "#d4a762", marginRight: 4 }}>✦</span>BAKKALAM<span style={S.logoSub}> MART</span></div>  
          <div style={S.tagline}>Premium Grocery · Kerala</div>  
        </div>  
        <div style={S.hRight}>  
          <div style={S.searchWrap}>  
            <span style={{ color: "#555", fontSize: 15 }}>⌕</span>  
            <input style={S.searchInput} placeholder="Search..." value={search} onChange={e => setSearch(e.target.value)} />  
          </div>  
          <button style={S.cartBtn} onClick={() => { setPage("cart"); setCheckout(false); }}>  
            🛒  
            {cartCount > 0 && <span style={S.badge2}>{cartCount}</span>}  
          </button>  
        </div>  
      </header>  
  
      <main style={S.main}>  
  
        {/* HOME */}  
        {page === "home" && <>  
          <div style={{ ...S.banner, background: OFFERS[offerIdx].bg }}>  
            <div>  
              <div style={{ ...S.bannerTitle, color: OFFERS[offerIdx].color }}>{OFFERS[offerIdx].title}</div>  
              <div style={S.bannerDesc}>{OFFERS[offerIdx].desc}</div>  
              <button style={{ ...S.bannerBtn, borderColor: OFFERS[offerIdx].color, color: OFFERS[offerIdx].color }}  
                onClick={() => setPage("shop")}>Shop Now →</button>  
            </div>  
            <span style={S.bannerEmoji}>🛒</span>  
            <div style={S.dots}>{OFFERS.map((_, i) => <span key={i} style={{ ...S.dot, background: i === offerIdx ? OFFERS[offerIdx].color : "#2a2a2a" }} />)}</div>  
          </div>  
  
          <div style={S.secHead}><span style={S.secTitle}>Categories</span><button style={S.seeAll} onClick={() => setPage("shop")}>See All</button></div>  
          <div style={S.catGrid}>  
            {CATEGORIES.slice(1).map(c => (  
              <button key={c.id} style={S.catCard} onClick={() => { setCat(c.id); setPage("shop"); }}>  
                <span style={{ fontSize: 26 }}>{c.icon}</span>  
                <span style={S.catLabel}>{c.label}</span>  
              </button>  
            ))}  
          </div>  
  
          <div style={S.secHead}><span style={S.secTitle}>Today's Picks</span><button style={S.seeAll} onClick={() => setPage("shop")}>See All</button></div>  
          <div style={S.grid2}>  
            {PRODUCTS.filter(p => p.badge === "Premium" || p.badge === "Fresh").slice(0, 6).map(p => (  
              <Card key={p.id} p={p} cart={cart} wishlist={wishlist} onAdd={() => addToCart(p)} onDec={() => dec(p.id)} onWish={() => toggleWish(p.id)} />  
            ))}  
          </div>  
        </>}  
  
        {/* SHOP */}  
        {page === "shop" && <>  
          <div style={S.secHead}>  
            <span style={S.secTitle}>All Products</span>  
            <select style={S.select} value={sort} onChange={e => setSort(e.target.value)}>  
              <option value="default">Default</option>  
              <option value="price-asc">Price ↑</option>  
              <option value="price-desc">Price ↓</option>  
            </select>  
          </div>  
          <div style={S.pills}>  
            {CATEGORIES.map(c => (  
              <button key={c.id} style={{ ...S.pill, ...(cat === c.id ? S.pillActive : {}) }} onClick={() => setCat(c.id)}>  
                {c.icon} {c.label}  
              </button>  
            ))}  
          </div>  
          <div style={S.grid2}>  
            {filtered.map(p => <Card key={p.id} p={p} cart={cart} wishlist={wishlist} onAdd={() => addToCart(p)} onDec={() => dec(p.id)} onWish={() => toggleWish(p.id)} />)}  
            {filtered.length === 0 && <div style={{ ...S.empty, gridColumn: "span 2" }}>No products found</div>}  
          </div>  
        </>}  
  
        {/* CART */}  
        {page === "cart" && !checkout && (  
          <div style={S.pad}>  
            <div style={S.secTitle}>Your Cart</div>  
            {cartItems.length === 0 ? (  
              <div style={S.emptyCart}>  
                <div style={{ fontSize: 60, marginBottom: 12 }}>🛒</div>  
                <div style={{ color: "#666", fontSize: 16, marginBottom: 20 }}>Your cart is empty</div>  
                <button style={S.goldBtn} onClick={() => setPage("shop")}>Start Shopping</button>  
              </div>  
            ) : <>  
              {cartItems.map(item => (  
                <div key={item.id} style={S.cartRow}>  
                  <img src={IMG[item.name]} alt={item.name}  
                    style={S.cartThumb}  
                    onError={e => { e.target.style.display = "none"; }} />  
                  <div style={{ flex: 1, minWidth: 0 }}>  
                    <div style={S.cartName}>{item.name}</div>  
                    <div style={{ fontSize: 11, color: "#555", marginBottom: 3 }}>{item.unit}</div>  
                    <div style={{ color: "#d4a762", fontWeight: "bold", fontSize: 14 }}>₹{item.price * item.qty}</div>  
                  </div>  
                  <div style={S.qRow}>  
                    <button style={S.qBtn} onClick={() => dec(item.id)}>−</button>  
                    <span style={{ color: "#f0ede8", fontWeight: "bold", minWidth: 20, textAlign: "center" }}>{item.qty}</span>  
                    <button style={S.qBtn} onClick={() => addToCart(item)}>+</button>  
                  </div>  
                </div>  
              ))}  
              <div style={S.summaryBox}>  
                <div style={S.sumRow}><span>Subtotal</span><span>₹{cartTotal}</span></div>  
                <div style={S.sumRow}><span>Delivery</span><span style={{ color: "#62d4a7" }}>Free</span></div>  
                <div style={{ ...S.sumRow, ...S.sumTotal }}><span>Total</span><span>₹{cartTotal}</span></div>  
                <button style={S.checkBtn} onClick={() => setCheckout(true)}>Proceed to Checkout →</button>  
              </div>  
            </>}  
          </div>  
        )}  
  
        {/* CHECKOUT */}  
        {page === "cart" && checkout && (  
          <div style={S.pad}>  
            <button style={S.back} onClick={() => setCheckout(false)}>← Back to Cart</button>  
            <div style={{ ...S.secTitle, marginBottom: 16 }}>Checkout</div>  
            <div style={S.summaryBox}>  
              <div style={{ color: "#555", fontSize: 11, letterSpacing: "0.08em", marginBottom: 8 }}>ORDER SUMMARY</div>  
              {cartItems.map(i => (  
                <div key={i.id} style={S.sumRow}>  
                  <span style={{ color: "#aaa", fontSize: 13 }}>{i.name} ×{i.qty}</span>  
                  <span style={{ color: "#d4a762" }}>₹{i.price * i.qty}</span>  
                </div>  
              ))}  
              <div style={{ ...S.sumRow, ...S.sumTotal }}><span>Total</span><span style={{ color: "#d4a762" }}>₹{cartTotal}</span></div>  
            </div>  
            {[  
              { label: "Full Name *", val: name, set: setName, ph: "Arjun Kumar", type: "text" },  
              { label: "Email *", val: email, set: setEmail, ph: "arjun@email.com", type: "email" },  
              { label: "Phone", val: phone, set: setPhone, ph: "+91 98765 43210", type: "tel" },  
              { label: "Delivery Address", val: addr, set: setAddr, ph: "House No, Street, City", type: "text" },  
            ].map(f => (  
              <div key={f.label} style={{ marginBottom: 12 }}>  
                <div style={S.label}>{f.label}</div>  
                <input style={S.input} placeholder={f.ph} type={f.type} value={f.val} onChange={e => f.set(e.target.value)} />  
              </div>  
            ))}  
            <button style={{ ...S.checkBtn, opacity: placing ? 0.6 : 1, marginTop: 8 }} onClick={placeOrder} disabled={placing}>  
              {placing ? "Placing Order..." : "✓ Place Order"}  
            </button>  
          </div>  
        )}  
  
        {/* ADMIN LOGIN */}  
        {page === "admin" && !adminAuthed && (  
          <div style={S.pad}>  
            <div style={{ textAlign: "center", paddingTop: 40, paddingBottom: 20 }}>  
              <div style={{ fontSize: 48, marginBottom: 12 }}>🔐</div>  
              <div style={{ ...S.secTitle, textAlign: "center", marginBottom: 6 }}>Admin Access</div>  
              <div style={{ color: "#555", fontSize: 13, marginBottom: 28 }}>Enter password to continue</div>  
            </div>  
            <div style={{ maxWidth: 320, margin: "0 auto" }}>  
              <div style={S.label}>Password</div>  
              <input  
                style={{ ...S.input, letterSpacing: "0.15em", fontSize: 18, textAlign: "center" }}  
                type="password"  
                placeholder="••••••••"  
                value={adminPass}  
                onChange={e => { setAdminPass(e.target.value); setAdminErr(""); }}  
                onKeyDown={e => {  
                  if (e.key === "Enter") {  
                    if (adminPass === ADMIN_PASS) { setAdminAuthed(true); setAdminPass(""); }  
                    else { setAdminErr("Incorrect password. Try again."); }  
                  }  
                }}  
              />  
              {adminErr && <div style={{ color: "#ef5350", fontSize: 13, marginTop: 8, textAlign: "center" }}>{adminErr}</div>}  
              <button style={{ ...S.checkBtn, marginTop: 16 }} onClick={() => {  
                if (adminPass === ADMIN_PASS) { setAdminAuthed(true); setAdminPass(""); }  
                else { setAdminErr("Incorrect password. Try again."); }  
              }}>Unlock Admin Panel</button>  
              <button style={{ ...S.back, display: "block", textAlign: "center", marginTop: 14, width: "100%" }} onClick={() => setPage("profile")}>← Go Back</button>  
            </div>  
          </div>  
        )}  
  
        {/* ADMIN PANEL */}  
        {page === "admin" && adminAuthed && (  
          <div style={S.pad}>  
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 4 }}>  
              <div style={S.secTitle}>Admin — Orders</div>  
              <div style={{ display: "flex", gap: 8 }}>  
                <button style={S.refreshBtn} onClick={loadOrders}>↻</button>  
                <button style={{ ...S.refreshBtn, color: "#d46262" }} onClick={() => { setAdminAuthed(false); setPage("profile"); }}>Lock</button>  
              </div>  
            </div>  
            <div style={{ color: "#555", fontSize: 11, marginBottom: 14, letterSpacing: "0.06em" }}>  
              {orders.length} total orders  
            </div>  
            <div style={S.statsGrid}>  
              {Object.entries(STATUS).map(([s, st]) => (  
                <div key={s} style={{ ...S.statCard, borderColor: st.color + "55" }}>  
                  <div style={{ fontSize: 20, fontWeight: "bold", color: st.color }}>{orders.filter(o => o.status === s).length}</div>  
                  <div style={{ fontSize: 9, color: "#666", marginTop: 2, letterSpacing: "0.04em" }}>{st.label}</div>  
                </div>  
              ))}  
            </div>  
            {ordersLoading ? (  
              <div style={{ textAlign: "center", paddingTop: 50 }}>  
                <div style={S.spinner} />  
                <div style={{ color: "#555", marginTop: 12, fontSize: 13 }}>Loading orders...</div>  
              </div>  
            ) : orders.length === 0 ? (  
              <div style={S.empty}>No orders yet. Share the app and start selling!</div>  
            ) : orders.map(o => (  
              <div key={o.id} style={S.orderCard}>  
                <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 10, gap: 8 }}>  
                  <div style={{ minWidth: 0 }}>  
                    <div style={{ fontWeight: "bold", fontSize: 15, marginBottom: 1 }}>{o.customer_name}</div>  
                    <div style={{ color: "#555", fontSize: 12, marginBottom: 1 }}>{o.customer_email}</div>  
                    <div style={{ color: "#333", fontSize: 11 }}>{new Date(o.created_at).toLocaleString()}</div>  
                  </div>  
                  <div style={{ textAlign: "right", flexShrink: 0 }}>  
                    <span style={{ ...S.statusChip, background: STATUS[o.status]?.bg, color: STATUS[o.status]?.color }}>  
                      {STATUS[o.status]?.label || o.status}  
                    </span>  
                    <div style={{ color: "#d4a762", fontWeight: "bold", fontSize: 17, marginTop: 5 }}>₹{o.total}</div>  
                  </div>  
                </div>  
                <div style={{ display: "flex", flexWrap: "wrap", gap: 5, marginBottom: 10 }}>  
                  {(o.items || []).map((item, i) => (  
                    <span key={i} style={S.itemChip}>{item.name} ×{item.qty}</span>  
                  ))}  
                </div>  
                <div style={{ display: "flex", gap: 5, flexWrap: "wrap" }}>  
                  {Object.entries(STATUS).map(([s, st]) => (  
                    <button key={s}  
                      style={{ ...S.sBtn, ...(o.status === s ? { background: st.color, color: "#0a0a0a", borderColor: st.color, fontWeight: "bold" } : {}) }}  
                      onClick={async () => {  
                        const { error } = await db.updateOrder(o.id, { status: s });  
                        if (error) toast("Update failed", "err");  
                        else { toast(`Marked as ${st.label}`); setOrders(prev => prev.map(x => x.id === o.id ? { ...x, status: s } : x)); }  
                      }}>  
                      {st.label}  
                    </button>  
                  ))}  
                </div>  
              </div>  
            ))}  
          </div>  
        )}  
  
        {/* PROFILE */}  
        {page === "profile" && (  
          <div style={S.pad}>  
            <div style={S.avatarWrap}>  
              <div style={S.avatar}>👤</div>  
              <div style={{ fontSize: 20, fontWeight: "bold", marginBottom: 3 }}>Welcome Back!</div>  
              <div style={{ color: "#555", fontSize: 13 }}>user@bakkalammart.com</div>  
            </div>  
            <div style={S.statsGrid}>  
              {[["Cart Items", cartCount], ["Wishlist", wishlist.size], ["Products", PRODUCTS.length]].map(([l, v]) => (  
                <div key={l} style={S.statCard}>  
                  <div style={{ fontSize: 22, fontWeight: "bold", color: "#d4a762" }}>{v}</div>  
                  <div style={{ fontSize: 10, color: "#555", marginTop: 2 }}>{l}</div>  
                </div>  
              ))}  
            </div>  
            <div style={{ background: "#111", border: "1px solid #1e1e1e", borderRadius: 16, overflow: "hidden" }}>  
              {["My Orders", "Delivery Addresses", "Payment Methods", "Notifications", "Help & Support"].map(item => (  
                <div key={item} style={S.menuItem}><span>{item}</span><span style={{ color: "#333" }}>›</span></div>  
              ))}  
              <div style={{ ...S.menuItem, color: "#d4a762", borderBottom: "none" }} onClick={goAdmin}>  
                <span>⚙ Admin Panel</span><span style={{ color: "#555" }}>›</span>  
              </div>  
            </div>  
          </div>  
        )}  
  
      </main>  
  
      {/* BOTTOM NAV */}  
      <nav style={S.nav}>  
        {[  
          { id: "home", icon: "⌂", label: "Home" },  
          { id: "shop", icon: "◫", label: "Shop" },  
          { id: "cart", icon: "🛒", label: "Cart", badge: cartCount },  
          { id: "profile", icon: "◎", label: "Profile" },  
        ].map(t => (  
          <button key={t.id} style={{ ...S.navTab, ...(page === t.id || (page === "admin" && t.id === "profile") ? S.navActive : {}) }}  
            onClick={() => { setPage(t.id); setCheckout(false); }}>  
            <span style={{ fontSize: 20, position: "relative", display: "inline-block" }}>  
              {t.icon}  
              {t.badge > 0 && <span style={S.navBadge}>{t.badge}</span>}  
            </span>  
            <span style={{ fontSize: 10, letterSpacing: "0.04em" }}>{t.label}</span>  
          </button>  
        ))}  
      </nav>  
    </div>  
  );  
}  
  
function Card({ p, cart, wishlist, onAdd, onDec, onWish }) {  
  const qty = cart[p.id] || 0;  
  const [imgError, setImgError] = useState(false);  
  return (  
    <div style={S.card}>  
      <button style={{ ...S.wishBtn, color: wishlist.has(p.id) ? "#e53935" : "#2a2a2a" }} onClick={onWish}>  
        {wishlist.has(p.id) ? "♥" : "♡"}  
      </button>  
      {p.badge && <div style={{ ...S.cardBadge, background: p.color + "30", color: p.color }}>{p.badge}</div>}  
      <div style={{ ...S.cardImgWrap, background: p.color + "12" }}>  
        {!imgError && IMG[p.name] ? (  
          <img  
            src={IMG[p.name]}  
            alt={p.name}  
            style={S.cardImg}  
            onError={() => setImgError(true)}  
          />  
        ) : (  
          <div style={{ fontSize: 36, lineHeight: 1 }}>  
            {p.category === "fruits" ? "🍎" : p.category === "vegetables" ? "🥦" : p.category === "bakery" ? "🍞" : p.category === "snacks" ? "🍟" : p.category === "beverages" ? "🥤" : p.category === "meat" ? "🍗" : "🛒"}  
          </div>  
        )}  
      </div>  
      <div style={S.cardBody}>  
        <div style={S.cardName}>{p.name}</div>  
        <div style={{ fontSize: 11, color: "#444", marginBottom: 8 }}>{p.unit}</div>  
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>  
          <span style={{ color: "#d4a762", fontWeight: "bold", fontSize: 15 }}>₹{p.price}</span>  
          {qty === 0  
            ? <button style={S.addBtn} onClick={onAdd}>+ Add</button>  
            : <div style={{ display: "flex", alignItems: "center", gap: 5 }}>  
                <button style={S.qSmBtn} onClick={onDec}>−</button>  
                <span style={{ color: "#f0ede8", fontWeight: "bold", minWidth: 18, textAlign: "center", fontSize: 13 }}>{qty}</span>  
                <button style={S.qSmBtn} onClick={onAdd}>+</button>  
              </div>  
          }  
        </div>  
      </div>  
    </div>  
  );  
}  
  
const S = {  
  root: { minHeight:"100vh", background:"#0a0a0a", color:"#f0ede8", fontFamily:"'Georgia','Times New Roman',serif", display:"flex", flexDirection:"column", maxWidth:480, margin:"0 auto", overflowX:"hidden" },  
  notif: { position:"fixed", top:68, left:"50%", transform:"translateX(-50%)", padding:"10px 22px", borderRadius:30, fontWeight:"bold", zIndex:1000, fontSize:13, whiteSpace:"nowrap", boxShadow:"0 4px 20px rgba(0,0,0,0.6)", letterSpacing:"0.02em" },  
  header: { display:"flex", justifyContent:"space-between", alignItems:"center", padding:"14px 16px 10px", background:"#0f0f0f", borderBottom:"1px solid #181818", position:"sticky", top:0, zIndex:100 },  
  logo: { fontSize:17, fontWeight:"bold", letterSpacing:"0.1em" },  
  logoSub: { fontSize:11, letterSpacing:"0.25em", color:"#d4a762", fontFamily:"monospace" },  
  tagline: { fontSize:10, color:"#3a3a3a", letterSpacing:"0.1em", marginTop:1 },  
  hRight: { display:"flex", alignItems:"center", gap:8 },  
  searchWrap: { display:"flex", alignItems:"center", gap:6, background:"#161616", border:"1px solid #1f1f1f", borderRadius:20, padding:"6px 12px" },  
  searchInput: { background:"none", border:"none", outline:"none", color:"#f0ede8", fontSize:13, width:100, fontFamily:"inherit" },  
  cartBtn: { background:"#161616", border:"1px solid #1f1f1f", borderRadius:12, padding:"8px 11px", cursor:"pointer", color:"#f0ede8", fontSize:18, position:"relative" },  
  badge2: { position:"absolute", top:-5, right:-5, background:"#d4a762", color:"#0a0a0a", borderRadius:"50%", width:17, height:17, fontSize:9, display:"flex", alignItems:"center", justifyContent:"center", fontWeight:"bold" },  
  main: { flex:1, overflowY:"auto", paddingBottom:80 },  
  banner: { margin:16, borderRadius:18, padding:"24px 20px 16px", position:"relative", overflow:"hidden", border:"1px solid #1a1a1a", minHeight:130 },  
  bannerTitle: { fontSize:22, fontWeight:"bold", letterSpacing:"0.03em", marginBottom:4 },  
  bannerDesc: { color:"#888", fontSize:14, marginBottom:14 },  
  bannerBtn: { border:"1px solid", borderRadius:20, padding:"7px 18px", fontSize:13, background:"transparent", cursor:"pointer", fontFamily:"inherit" },  
  bannerEmoji: { position:"absolute", right:20, top:"50%", transform:"translateY(-50%)", fontSize:50, opacity:0.2 },  
  dots: { display:"flex", gap:5, marginTop:12 },  
  dot: { width:6, height:6, borderRadius:"50%", transition:"background 0.3s" },  
  secHead: { display:"flex", justifyContent:"space-between", alignItems:"center", padding:"16px 16px 8px" },  
  secTitle: { fontSize:18, fontWeight:"bold", letterSpacing:"0.04em" },  
  seeAll: { background:"none", border:"none", color:"#d4a762", fontSize:13, cursor:"pointer", fontFamily:"inherit" },  
  catGrid: { display:"grid", gridTemplateColumns:"repeat(4,1fr)", gap:10, padding:"0 16px" },  
  catCard: { background:"#111", border:"1px solid #1a1a1a", borderRadius:14, padding:"14px 4px", display:"flex", flexDirection:"column", alignItems:"center", gap:6, cursor:"pointer" },  
  catLabel: { fontSize:9, color:"#888", textAlign:"center", lineHeight:1.2 },  
  grid2: { display:"grid", gridTemplateColumns:"repeat(2,1fr)", gap:12, padding:"0 16px" },  
  pills: { display:"flex", gap:8, padding:"0 16px 12px", overflowX:"auto", scrollbarWidth:"none" },  
  pill: { background:"#111", border:"1px solid #1a1a1a", borderRadius:20, padding:"7px 13px", fontSize:12, color:"#666", cursor:"pointer", whiteSpace:"nowrap", fontFamily:"inherit", flexShrink:0 },  
  pillActive: { background:"#d4a762", color:"#0a0a0a", borderColor:"#d4a762", fontWeight:"bold" },  
  select: { background:"#111", border:"1px solid #1a1a1a", color:"#aaa", borderRadius:10, padding:"6px 10px", fontSize:12, fontFamily:"inherit", cursor:"pointer" },  
  empty: { color:"#444", textAlign:"center", padding:40 },  
  card: { background:"#0f0f0f", border:"1px solid #1a1a1a", borderRadius:16, overflow:"hidden", position:"relative" },  
  wishBtn: { position:"absolute", top:8, right:8, background:"none", border:"none", fontSize:17, cursor:"pointer", zIndex:2 },  
  cardBadge: { position:"absolute", top:8, left:8, fontSize:9, padding:"3px 8px", borderRadius:10, fontWeight:"bold", zIndex:2, letterSpacing:"0.04em" },  
  cardImgWrap: { height:110, display:"flex", alignItems:"center", justifyContent:"center", overflow:"hidden" },  
  cardImg: { width:"100%", height:"100%", objectFit:"cover" },  
  cardBody: { padding:"10px 12px 12px" },  
  cardName: { fontSize:12, fontWeight:"600", lineHeight:1.3, marginBottom:2, color:"#e0ddd8" },  
  addBtn: { background:"#d4a762", color:"#0a0a0a", border:"none", borderRadius:10, padding:"5px 11px", fontSize:12, fontWeight:"bold", cursor:"pointer", fontFamily:"inherit", flexShrink:0 },  
  qSmBtn: { background:"#1a1a1a", border:"1px solid #252525", color:"#d4a762", borderRadius:7, width:24, height:24, display:"flex", alignItems:"center", justifyContent:"center", cursor:"pointer", fontSize:14, fontWeight:"bold" },  
  pad: { padding:16 },  
  emptyCart: { textAlign:"center", paddingTop:60 },  
  goldBtn: { background:"#d4a762", color:"#0a0a0a", border:"none", borderRadius:14, padding:"12px 32px", fontSize:15, fontWeight:"bold", cursor:"pointer", fontFamily:"inherit" },  
  cartRow: { display:"flex", alignItems:"center", gap:12, background:"#0f0f0f", border:"1px solid #1a1a1a", borderRadius:16, padding:10, marginBottom:10, overflow:"hidden" },  
  cartThumb: { width:58, height:58, borderRadius:10, objectFit:"cover", flexShrink:0 },  
  cartName: { fontSize:13, fontWeight:"600", marginBottom:2, whiteSpace:"nowrap", overflow:"hidden", textOverflow:"ellipsis" },  
  qRow: { display:"flex", alignItems:"center", gap:7, flexShrink:0 },  
  qBtn: { background:"#1a1a1a", border:"1px solid #252525", color:"#d4a762", borderRadius:9, width:28, height:28, display:"flex", alignItems:"center", justifyContent:"center", cursor:"pointer", fontSize:15, fontWeight:"bold" },  
  summaryBox: { background:"#0f0f0f", border:"1px solid #1a1a1a", borderRadius:16, padding:16, marginTop:14 },  
  sumRow: { display:"flex", justifyContent:"space-between", color:"#777", fontSize:13, marginBottom:8 },  
  sumTotal: { color:"#f0ede8", fontSize:16, fontWeight:"bold", borderTop:"1px solid #1a1a1a", paddingTop:10, marginTop:4 },  
  checkBtn: { width:"100%", background:"linear-gradient(135deg,#d4a762,#b8843a)", color:"#0a0a0a", border:"none", borderRadius:14, padding:14, fontSize:15, fontWeight:"bold", cursor:"pointer", marginTop:14, fontFamily:"inherit", letterSpacing:"0.04em" },  
  back: { background:"none", border:"none", color:"#d4a762", fontSize:14, cursor:"pointer", fontFamily:"inherit", marginBottom:12, padding:0 },  
  label: { fontSize:11, color:"#666", marginBottom:5, letterSpacing:"0.06em" },  
  input: { width:"100%", background:"#0f0f0f", border:"1px solid #1f1f1f", borderRadius:12, padding:"12px 14px", color:"#f0ede8", fontSize:14, fontFamily:"inherit", outline:"none" },  
  statsGrid: { display:"grid", gridTemplateColumns:"repeat(4,1fr)", gap:8, marginBottom:16 },  
  statCard: { background:"#0f0f0f", border:"1px solid", borderRadius:12, padding:"12px 4px", textAlign:"center" },  
  refreshBtn: { background:"#111", border:"1px solid #1f1f1f", color:"#d4a762", borderRadius:10, padding:"7px 14px", fontSize:13, cursor:"pointer", fontFamily:"inherit" },  
  spinner: { width:30, height:30, border:"3px solid #1a1a1a", borderTop:"3px solid #d4a762", borderRadius:"50%", animation:"spin 0.8s linear infinite", margin:"0 auto" },  
  orderCard: { background:"#0f0f0f", border:"1px solid #1a1a1a", borderRadius:16, padding:14, marginBottom:12 },  
  statusChip: { fontSize:10, padding:"3px 10px", borderRadius:10, fontWeight:"bold", display:"inline-block", letterSpacing:"0.04em" },  
  itemChip: { background:"#161616", border:"1px solid #1f1f1f", borderRadius:8, padding:"3px 8px", fontSize:11, color:"#888" },  
  sBtn: { background:"#111", border:"1px solid #1f1f1f", color:"#666", borderRadius:8, padding:"5px 10px", fontSize:11, cursor:"pointer", fontFamily:"inherit" },  
  avatarWrap: { textAlign:"center", paddingBottom:20 },  
  avatar: { width:80, height:80, background:"#161616", border:"2px solid #d4a762", borderRadius:"50%", display:"flex", alignItems:"center", justifyContent:"center", margin:"0 auto 12px", fontSize:44 },  
  menuItem: { display:"flex", justifyContent:"space-between", alignItems:"center", padding:"15px 18px", borderBottom:"1px solid #161616", fontSize:14, color:"#bbb", cursor:"pointer" },  
  nav: { display:"flex", position:"fixed", bottom:0, left:"50%", transform:"translateX(-50%)", width:"100%", maxWidth:480, background:"#0d0d0d", borderTop:"1px solid #181818", zIndex:100 },  
  navTab: { flex:1, background:"none", border:"none", padding:"10px 0 8px", display:"flex", flexDirection:"column", alignItems:"center", gap:3, cursor:"pointer", color:"#333", fontFamily:"inherit", position:"relative" },  
  navActive: { color:"#d4a762" },  
  navBadge: { position:"absolute", top:-5, right:-6, background:"#d4a762", color:"#0a0a0a", borderRadius:"50%", width:15, height:15, fontSize:8, display:"flex", alignItems:"center", justifyContent:"center", fontWeight:"bold" },  
};  
