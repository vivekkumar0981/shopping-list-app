package com.example.myshoppinglist

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


//data class shopingItem used everywhere in program

data class ShopingItem(
    var id:Int,
    var name:String,
    var quantity:Int,
    var isEditing:Boolean=false

)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp() {
    var showdialog by remember { mutableStateOf(false) }

    var sItems by remember { mutableStateOf(listOf<ShopingItem>()) }

    var ItemName by remember { mutableStateOf("")}
    var ItemQuantity by remember { mutableStateOf("")}






    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    )
    {

        Button(
            onClick = { showdialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        {

            Text(text = "Add Item")

        }

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        items(sItems) {

            item ->
            if(item.isEditing){
                
                shoppingItemEditor(Item =item , onEditComplete = {
                    editName,editQuantity ->
                    sItems=sItems.map { it.copy(isEditing =false)  }
                    val editItem=sItems.find { it.id == item.id  }
                    editItem?.let {
                        it.name=editName
                        it.quantity=editQuantity
                    }

                })


            }
            else
            {
                shoppingListItem(item =item , onEditClick = {

                    sItems=sItems.map { it.copy(isEditing = it.id ==item.id) } },

                    onDeleteClick = {sItems=sItems-item}

                    )
            }

        }
    }
}
        if(showdialog)
        {
            // alert dialog is poped up when we click a button show that we can

            AlertDialog(
                onDismissRequest = {showdialog=false },
                confirmButton =
                {
                   Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                       .fillMaxWidth()
                       .padding(8.dp))
                   {


                       // button for save option in aleart box
                       Button(onClick =
                       {
                           if (ItemName.isNotBlank())
                           {
                               val newItem =ShopingItem(
                                   id=sItems.size+1,
                                   name=ItemName,
                                   quantity = ItemQuantity.toInt(),
                                   isEditing = false

                               )
                               sItems=sItems+newItem
                               showdialog=false
                               ItemName=""
                           }
                       })
                       {
                           Text(text = "Save")
                       }

                       // button for cancel button in aleart box


                       Button(onClick = {showdialog=false})
                       {
                           Text(text = "Cancel")
                       }
                   }
                },
                title = { Text(text = "Add Shopping Items")},

                text = {
                    Column {
                        OutlinedTextField(
                            value =ItemName,
                            onValueChange ={ItemName=it} ,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            singleLine = true,
                            label = { Text(text = "Enter the value")}

                        )
                        OutlinedTextField(
                            value = ItemQuantity,
                            onValueChange ={ItemQuantity=it},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            singleLine = true,
                            label = { Text(text = "Enter the Quantity")}
                            )
                    }
                }
            )
        }

}

@Composable
fun shoppingListItem(
    item:ShopingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(border = BorderStroke(2.dp, Color(0XFF018786)), shape = RoundedCornerShape(20))
    )
    {
        Text(text =item.name, modifier = Modifier.padding(8.dp))
        
        Text(text = "Qty.  ${item.quantity} ", modifier = Modifier.padding(8.dp))

        Row(modifier = Modifier.padding(8.dp)) {
            
            IconButton(onClick = onEditClick ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editable icon" )
            }
            
            IconButton(onClick = onDeleteClick ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription =null )

            }

        }
    }

}

@Composable
fun shoppingItemEditor(Item:ShopingItem,onEditComplete : (String,Int) ->Unit)
{
    var editName by remember { mutableStateOf(Item.name)}
    var editQuantity by remember { mutableStateOf(Item.quantity.toString())}

    var isEditing by remember { mutableStateOf(Item.isEditing)}

    Row (modifier = Modifier
        .padding(8.dp)
        .background(Color.White)
        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
    {

        Column {
            
            BasicTextField(value = editName,
                onValueChange = {editName=it},
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize())

            BasicTextField(value = editQuantity,
                onValueChange = {editQuantity=it},
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize())



        }
        Button(onClick = {

            isEditing=false
            onEditComplete(editName,editQuantity.toIntOrNull() ?: 1)


        }) {

            Text(text = "Save")
        }

    }

}



