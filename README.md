# shopServer2
This is the server end of our YIBEE student second-hand goods trading website.       
Enter the website here: http://54.144.36.138/

## Genernal Introduction and aim of the website
This website provides a platform for students across the United States to trade their second-hand goods like furnitures, clothes, books and othe stuff. The target populations of our website are students in colleges, they are moving all the times with huge need of selling and buying second-hand goods.

This is full-stack web development project mainly using Spring Boot framework in back end and React in front end. Designing and working out the whole project really enhance my skills in full stack web dev fileds and give me a better understanding responding to bussiness need. Hope you like it!

## Project Architecture
We used React framework and typescript to develop the client end of the website. Consumed REST APIs using jQuery's ajax() Function for Asynchronous HTTP Requests. Designed SPA(Single-Paged Application) routing system in app.tsx and utilized Ant Design and CSS for page layout and design.

## Modules and Functions
This is our navigation bar!!! Users can access to main modules and get messages alert!
![image](https://github.com/Shichao97/README-Image/blob/master/navigation.png)

#### Register and activate Accounts 
Users can register for free member from this page:
![image](https://github.com/Shichao97/README-Image/blob/master/register.png)

After successfully registration, users will get an activation email, they can click on the link to get activated.

#### Login 
When users are not logged in, they can click on login link on the right side of navigation bar. Ans Users will receive poped up login alert when they want to access modules(excluding searching goods). 

![image](https://github.com/Shichao97/README-Image/blob/master/login2.png)
![image](https://github.com/Shichao97/README-Image/blob/master/login.png)

When users forget to write some of the part or get some error, they will get in time alert.
When users forget password, they can reset password beside login window through email.


#### Logout and Avatar Edit 
When clicking on user avatar on the right side of navagation bar, users logout account or change to other avatars.
![image](https://github.com/Shichao97/README-Image/blob/master/logout_and_avatar.png)


#### Add Goods
When clicking on "Add Goods" link in the middle of navagation bar, users can add second-hand goods one by one. Users can upload up to 16 images and they shoul be free to click on any images they've already add to delete.
![image](https://github.com/Shichao97/README-Image/blob/master/addgoods.png)

#### Goods Searching
Both registered users and guests can search goods from our website. They can choose schools and use goods name for searching. 
![image](https://github.com/Shichao97/README-Image/blob/master/searchgoods1.png)
![image](https://github.com/Shichao97/README-Image/blob/master/searchgoods2.png)

Goods are diaplyed as cards, and users can turning page when there are a large amount of goods.


#### Edit and Buy Goods
When user click on the goods cards they owned after searching, they can see the details of their goods and do editing and removing off from shelf later.
![image](https://github.com/Shichao97/README-Image/blob/master/edit1.png)

Also, when user click on other goods, they can see detail info of goods and like,buy the goods or talk to seller.(Detailed info in My Collection and Message System part)
![image](https://github.com/Shichao97/README-Image/blob/master/buy_goods.png)

After users have clicked on buy now button, they can palce order and pay for it:
![image](https://github.com/Shichao97/README-Image/blob/master/buygoods.png)
![image](https://github.com/Shichao97/README-Image/blob/master/confirm_buy.png)

![image](https://github.com/Shichao97/README-Image/blob/master/payment.png)

If users try to leave payment page they will get alert:
![image](https://github.com/Shichao97/README-Image/blob/master/leave_payment.png)

After successfully payment, users will get a order id and they can click on to get detailed order information.
![image](https://github.com/Shichao97/README-Image/blob/master/payment_success.png)

#### My Orders
My orders module on our website give users full view of orders they have. Users can see red number alert for orders that they haven't been paid or not finished yet. And they also can see all the orders they have and turning page.
![image](https://github.com/Shichao97/README-Image/blob/master/myorderd.png)
Users can click on image and name for getting access to order details. They can confirm receiving(finish the order) for orders that are in progress and pay for orders that they haven't paid.

#### My Selling
My orders module on our website give users full view of selling status of their own goods. Users can see red number alert for goods that are selling now and orders that are on the way (buyers already for the order but hasn't received yet).And they also can see all
the orders they have already sold out and goods that are removed from the shlf.Turning page available.
![image](https://github.com/Shichao97/README-Image/blob/master/myselling.png)


#### My Collection
Users can click on like and dislike heart-shaped button to collect goods or remove from their collection.
![image](https://github.com/Shichao97/README-Image/blob/master/like_and_dislike.png)

And in my collect module, they can search all the goods they collect by name.
![image](https://github.com/Shichao97/README-Image/blob/master/my_collection.png)

#### Message System
Users will get a red notice on the right side on the navigation bar when they receive messages from other users.
When users get into detailed information of goods, they can click on talk to seller and get a talk:
![image](https://github.com/Shichao97/README-Image/blob/master/talk_to_seller.png)

Also when there are some changes about their goods and orders, they will get real-time push message from system.

![image](https://github.com/Shichao97/README-Image/blob/master/message_system.png)

