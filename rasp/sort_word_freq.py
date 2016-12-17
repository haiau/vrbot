from pymongo import MongoClient
from sets import Set
from bson.int64 import Int64

words = Set()
client = MongoClient('localhost', 27017)
db = client.vreader

# f1 = 0
# f2 = 0
# for res in db.words.find({}):
#     words.add(res['w'])
#
# print "Create set"
#
# for word in words:
#     for cs in db.single_word_freq.find({"w": word}):
#         db.sortedlist.insert({"w": cs['w'], "f": Int64(cs['f'])})


# fo = open("/home/haiau/Desktop/words_edited.txt", "w")
with open("/home/haiau/Desktop/words.csv") as fi:
    content = fi.readlines()
    #content = content.replace('\n', '')
    for line in content:
        line = ''.join(line).replace('\n','')
        if (db.single_word_freq.find({"w": line}).count() > 0):
            # words.add(line)
            print (line)
            db.filter.insert({"w": line})










