import hashlib
import requests
import Spider
from bs4 import BeautifulSoup 
#Add your seed link here
import pdb
ROOT = 'http://zeenews.india.com'
from masterConfig import mconfig


def get_artical_info(url):
    try:
        #Build the soup from URL
        soup = Spider.buildSoup(url)
        if soup == None :
            return None
        
        #define the prory to retrive from soup.
        info2 = {
                    'title':['.full-story-head','text','JOIN'],
                    'date':['.writer','text','JOIN'],
                    'details':['.full-con p','text','JOIN'],
                    'images':['.full-con img','src'],
                    'video':['iframe','src']
                }
                
        #Get the info from soup.
        x = Spider.getAttrListForXPath(soup,'div.connrtund', None,info2)
        if x == None:
            return None
        info = x[0]
        
        #Make some modification on info.
        if info.get('images') != None:
            new =[]
            for f in info['images']:
                if f.startswith('/'):
                    new.append(ROOT+ f)
                else:
                    new.append(f)
            info['images'] = new
            info['head_image'] = new[0]
        
        return info
    except Exception, e:
        print 'Error in (get_artical_info): ',url,str(e)
        return None

def get_all_artical_links(url):
    try:
        print 'getting',url
        html = requests.get(url)
        if html.status_code != 200:
            print 'Error: Can;t retrive artical'
            return None
        #pdb.set_trace();
        soup = BeautifulSoup(html.text)
         
        links =[]
        #pdb.set_trace()
        x = soup.find("div", { "class" : "lead-health" })
        if x: links.append(x.find('a').get('href'))
 

        links += [ y.find('a').get('href') for y in soup.find_all("div", { "class" : "lead-health-nw" }) if y.find('a')]
        
        global ROOT
        return [ ROOT + l for l in links if l[0] == '/']
                  
         
    except Exception, e:
        print 'Error in Group',url,str(e)
        return None
 
def test():
    i = get_artical_info('http://zeenews.india.com/bengali/lifestyle/fight-for-saving-friends-life_147105.html')
    pdb.set_trace()
    
    
#test();

