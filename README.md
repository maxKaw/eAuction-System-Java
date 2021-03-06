# eAuction System Java

 eAuction system is a software built with Java programming language with the use of the best practices. 
 The whole system was written in accordance with Object-Oriented programming paradigm, so even though it is console-based application, it can be easily embedded in any other environment.
 
 eAuction system is a typical system with auctions where the registered users can buy and sell various items by a bidding system.
 The whole system is based on multithreading and concurrency, so multiple auctions are going on in the background.
 There are some initially registered users who are saved in serialized file where the objects are represented by a sequence of bytes, and it is deserialized and loaded in at every start of the application.
 The registration of the new users is pretty basic, but it allows to register new accounts and to choose their role in the system; everyone can choose between buyer and seller.

 Each item in the system is assigned to a seller initially, which can be put on an auction with parameters such as price, reserve price, and closing date and time.
 All the users are constantly informed about the expiring auctions as well as auctions that are still active. 

 There is also an administrator which is capable of changing the duration of thread sleep that is responsible for checking the status of auctions as well as blocking ongoing auctions and sellers. 
 
## List of registered users

Buyers:
- Username: Bob, Password: 123
- Username: Bill, Password: 123
- Username: Ben, Password: 123

Sellers:
- Username: Steve, Password: 123
- Username: Simon, Password: 123

Admin:
- Username: Glyn, Password: 123a

# Screenshots of the console application
Buyer menu</br>
<img src="Screenshots/screenshot1.png"></img></br></br>
Seller menu</br>
<img src="Screenshots/screenshot2.png"></img></br></br>
Active auctions</br>
<img src="Screenshots/screenshot3.PNG"></img></br></br>
Process of adding an item</br>
<img src="Screenshots/screenshot4.PNG"></img></br></br>
Update from the system about the finished auction and still active auctions</br>
<img src="Screenshots/screenshot5.PNG"></img>
