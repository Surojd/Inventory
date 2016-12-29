Inventory API
====================

/json/{table} for get all data form table => with GET METHOD

/json/{table}/{id} for get data by id form table => with GET METHOD

/json/{table} 
one data insert
request body 
{
  "title"= "inventory"
}
for insert data in table => with POST METHOD


/json/{table} 
bulk data insert
request body 
[{
  "title"= "inventory"
},
{
  "title"= "inventory"
},
{
  "title"= "inventory"
}]
for insert data in table => with POST METHOD

