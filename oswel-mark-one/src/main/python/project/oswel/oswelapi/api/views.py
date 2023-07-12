from rest_framework import status, serializers
from rest_framework.response import Response
from django.shortcuts import get_object_or_404
from rest_framework.decorators import api_view
from base.models import Item
from .serializers import ItemSerializer

@api_view(['GET'])
def ApiOverview(request):
    api_urls = {
        'all_items': '/all',
        'Search by Category': '/get/<category_name>',
        'Add': '/add',
        'Update': '/update/<category_name>',
        'Delete': '/delete/<category_name>'
    }
 
    return Response(api_urls)

@api_view(['GET'])
def getItems(request):
    
    # checking for the parameters from the URL
    if request.query_params:
        items = Item.objects.filter(**request.query_params.dict())
    else:
        items = Item.objects.all()
 
    # if there is something in items else raise error
    if items:
        serializer = ItemSerializer(items, many=True)
        return Response(serializer.data)
    else:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
@api_view(['GET'])
def getItem(request, category):

    items = Item.objects.all()
    item = get_object_or_404(items, category=category)
    serializer = ItemSerializer(item)
    return Response(serializer.data)
   

@api_view(['POST'])
def addItem(request):
    serializer = ItemSerializer(data=request.data)

    # validating for already existing data
    if Item.objects.filter(**request.data).exists():
        raise serializers.ValidationError('This data already exists')
    
    if serializer.is_valid():
        serializer.save()       
        return Response(serializer.data)
    else:
        return Response(status=status.HTTP_404_NOT_FOUND)


@api_view(['POST'])
def updateItem(request, category):

    # checking for the parameters from the URL
    item = Item.objects.get(category=category)
    serializer = ItemSerializer(instance=item, data=request.data)
 
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data)
    else:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
@api_view(['DELETE'])
def deleteItem(request, category):
    # checking for the parameters from the URL
    item = Item.objects.get(category=category)
    item.delete()
    return Response(status=status.HTTP_202_ACCEPTED)