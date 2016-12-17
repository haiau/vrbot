from pymongo import MongoClient
from sets import Set
from bson.int64 import Int64

words = Set()
client = MongoClient('localhost', 27017)
db = client.vreader


i = 0
for cs in db.filter.find({}):
    word = ''.join(cs['w'])
    # word = word.replace('\n','')
    if (i%10 == 0):
        print "\n",
    print word,
    i = i + 1









