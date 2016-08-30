#Moving to Spider

import requests
from bs4 import BeautifulSoup 
import Spider
#Add your seed link here
import pdb
ROOT = 'http://www.anandabazar.com'

def get_artical_info(url):
    try:
        #Build the soup from URL
        soup = Spider.buildSoup(url)
        if soup == None :
            return None
        
        #define the prory to retrive from soup.
        info2 = {
                    'title':['h1','text','NO_LIST_IF_ONE'],
                    'date':['.publish_date','text'],
                    'details':['.articleBody','text'],
                    'images':['img','src'],
                    'video':['iframe','src']
                }
                
        #Get the info from soup.
        x = Spider.getAttrListForXPath(soup,'#story_container', None,info2)
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

def first_page_parser(url):
    #pdb.set_trace();
    try:
        #Build the soup from URL
        soup = Spider.buildSoup(url)
        if soup == None :
            return None
            
        #Get the info from soup.
        x1 = Spider.getAttrListForXPath(soup,'div.leadstoryheading', None,{'url':['a','href']})
        
        #Process:
        res =[ ]
        for link in x1:
            s = link.get('url')[0]
            if s.startswith('/'):
                res.append(ROOT+s)
                
        return res;        
    except Exception, e:
        print '[ERROR] get_all_artical_links_first_page',url,str(e)
        return None
            
def get_all_artical_links(url):
    try:
        #Build the soup from URL
        soup = Spider.buildSoup(url)
        if soup == None :
            return None
            
        #Get the info from soup.
        x1 = Spider.getAttrListForXPath(soup,'div.leadstory-section-heading', None,{'url':['a','href']})
        x2 = Spider.getAttrListForXPath(soup,'div.sectionstoryinside-sub', None,{'url':['a','href']})
        
        #Process:
        res =[ ]
        for link in x1+x2:
            s = link.get('url')[0]
            if s.startswith('/'):
                res.append(ROOT+s)
                
        return res;  
    except Exception, e:
        print 'Error in Group',url,str(e)
        return None
        
        
def test():
    #i = get_artical_info('http://www.anandabazar.com/entertainment/i-didn-t-know-i-am-so-seductive-says-swastika-mukherjee-dgtl-1.459800#')
    #i = first_page_parser("http://www.anandabazar.com");
    i = get_all_artical_links("http://www.anandabazar.com/calcutta");
    print i
    pdb.set_trace()
    
    
#test();
 
 


