# OswelM
 Ordinary Systems of a Well Established Language Model 





# Python
Creating the server:
django-admin startproject oswelapi
add 'rest_framework' under ./oswelapi/oswelapi/settings.py => INSTALLED_APPS

This command creates an sqlite database
python manage.py startapp base
add 'base' under settings.py aswell

python manage.py makemigrations
python manage.py migrate

Test Database
python manage.py shell

>>> from base.models import Item
>>> Item.objects.create(name="Item #1") 
<Item: Item object (1)>
>>> Item.objects.create(name="Item #2") 
<Item: Item object (2)>
>>> Item.objects.create(name="Item #3") 
<Item: Item object (3)>
>>> items = Item.objects.all()
>>> print(items)
<QuerySet [<Item: Item object (1)>, <Item: Item object (2)>,<Item: Item object (3)>]>



starting the server:
python manage.py runserver



http://127.0.0.1:8000/api/all/
http://127.0.0.1:8000/api/all/?category=<category_name>
http://127.0.0.1:8000/api/add/
http://127.0.0.1:8000/api/update/<category_name>
http://127.0.0.1:8000/api/delete/<category_name>
# Java