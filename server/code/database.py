import pymongo
import pdb
from pymongo import MongoClient

def printf(a):
    print a

def norm(a):
    del a['_id']
    return a;
    
class Store(object):
    __instance = None
    conn = None
    def __new__(cls):
        if Store.__instance is None:
            Store.__instance = object.__new__(cls)
            client = MongoClient('localhost', 27018)
            db = client['flipnews_database']
            cls.conn = db['flipnews_collection']
            printf('>>>>> DATA BASE CONNECTION DONE <<<<< ')
            
        return Store.__instance
    def __init__(self):
        printf('callint __init__')
        pass

    def getConn(self):
        return Store.conn
    
    #return True or False: tested
    def is_exist(self,url):
        return Store.conn.find({"url": url}).count() > 0
    
    #Insert Unique URl : Tested
    def insert(self, data):
        url = data['url']
        if not self.is_exist(url):
            Store.conn.insert(data)
            return True
        else:
            printf('Info: Alread exits : '+url)
            return False
            
    #Pagination get: Tested
    def get(self, page = 1,limit =10): #page 1...
        return [  norm(x)  for x in Store.conn.find().sort([("_id", pymongo.DESCENDING)]).skip(limit*(page-1)).limit(limit)]

    def getByCat(self, tags = 'calcutta', page = 1,limit =10): #page 1...  
        return [  norm(x)  for x in self.conn.find({'categories' : tags }).sort([("_id", pymongo.DESCENDING)]).skip(limit*(page-1)).limit(limit) ]

    def getBySource(self, tags = 'calcutta', page = 1,limit =10): #page 1...
        return [  norm(x)  for x in self.conn.find({'source' : tags }).sort([("_id", pymongo.DESCENDING)]).skip(limit*(page-1)).limit(limit) ]
        
    def getByDate(self, date = '08-10-2016', page = 1,limit =10): #page 1...
        #pdb.set_trace()
        return [  norm(x)  for x in self.conn.find({'date' : date }).sort([("_id", pymongo.DESCENDING)]).skip(limit*(page-1)).limit(limit) ]
        
def test():   
    #Test single ton
    s1 = Store()
    c1 = s1.getConn()

    #Test Inserting
    obj = {'url':'abc','tags':'hello'}
    print s1.insert(obj)
    print s1.insert(obj)
    print s1.is_exist('abc')
    print s1.is_exist('abc1')

    #Test get and pagination
    for i in range(20):
        s1.insert({'url':'abc'+str(i),'tags':'hello'})
    print s1.get(1,3)
    print s1.get(2,3)
    print s1.get(3,3)
    print s1.get(4,3)
    print s1.get(5,3)
    print s1.get(6,3)

    #Test get by tagging..
    s1.insert({'url':'goo'+str(i),'tags':'goo'})
    s1.insert({'url':'foo'+str(i),'tags':'foo'})
    s1.insert({'url':'foo goo'+str(i),'tags':'foo goo'})

    print s1.getByCat('goo')
    print s1.getByCat('foo')
    print s1.getByCat('foo goo')
    print s1.getByCat('goo foo')
  
#pdb.set_trace()
