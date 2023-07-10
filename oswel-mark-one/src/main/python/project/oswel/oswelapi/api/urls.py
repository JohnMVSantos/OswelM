from django.urls import path
from . import views


urlpatterns = [
    path('', views.ApiOverview, name='home'),
    path('all/', views.getData, name='view-items'),
    path('add/', views.addItem, name="add-item"),
    path('update/<str:category>/', views.updateItem, name='update-item'),
    path('delete/<str:category>/', views.deleteItem, name='delete-item'),
]