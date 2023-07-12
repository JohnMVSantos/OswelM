from rest_framework import serializers
from django.db.models import fields
from base.models import Item

class ItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = Item
        fields = ('category', 'description', 'created')
       
        