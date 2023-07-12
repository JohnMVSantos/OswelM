from django.urls import path
from . import views


urlpatterns = [
    path('', views.ApiOverview, name='home'),
    path('all/', views.getItems, name='view-items'),
    path('add/', views.addItem, name="add-item"),
    path('get/<str:category>/', views.getItem, name='get-item'),
    path('update/<str:category>/', views.updateItem, name='update-item'),
    path('delete/<str:category>/', views.deleteItem, name='delete-item'),
]