import hashlib
import requests
from bs4 import BeautifulSoup 
#Add your seed link here
import pdb
ROOT = 'http://eisamay.indiatimes.com'
from masterConfig import mconfig

def remove_duplicates(a):
     return [ x for x in iter(set(a)) ]
     
     
def get_artical_info(url): #Working..
    print '>>> Doing.. get_artical_info : ',url
    try:
        print 'getting',url
        html = requests.get(url)
        if html.status_code != 200:
            print 'Error: Can;t retrive artical'
            return None
        #pdb.set_trace();
        soup = BeautifulSoup(html.text)
         
        story_container =  soup.find("div",{'class':'contentarea'}) 
     
        title = story_container.find('h1').text
        date =  None
        details = story_container.find("arttextxml").text
        images = [ m.get('src') for m in story_container.find('div', {'class':'articleImage'}).find_all('img') if m.get('src').endswith('jpg')]
        head_image = images[0] if len(images) > 0 else None
        
        return {'title':title,'date':date,'details':details,'images':images,'head_image':head_image}
    except Exception, e:
        print 'Error in (get_artical_info): ',url,str(e)
        return None
        
      
 
def get_all_artical_links(url):
    print '>>> Doing.. get_all_artical_links : ',url
    try:
        print 'getting',url
        html = requests.get(url)
        if html.status_code != 200:
            print 'Error: Can;t retrive artical'
            return None
        #pdb.set_trace();
        soup = BeautifulSoup(html.text)
         
        links =[]
        page_container = soup.find('div',{'class':'contentarea'})

        links += remove_duplicates([ y.find('a').get('href') for y in page_container.find("div",{'class':'mainarticle1'}) if y.find('a')])
        
        links += remove_duplicates([ y.find('a').get('href') for y in page_container.find("div",{'class':'other_main_news1'}).find_all('li') if y.find('a')])
        
        links += remove_duplicates([ y.find('a').get('href') for y in page_container.find("div",{'class':'topmain'}).find_all('div') if y.find('a')])
        
        global ROOT
        x1 = [ ROOT + l for l in links if l[0] == '/']
        x2 = [ l for l in links if l[0] != '/'] 
        return x1 + x2
         
    except Exception, e:
        print 'Error in (get_all_artical_links): ',url,str(e)
        return None
 
 
#All Interface must implemnets this...
import random,string
def get_all_data_for_a_seed(seed):
    try:
        links = get_all_artical_links(seed['url'])
        if not links:
            return None
        res = []
        #pdb.set_trace()
        ii =0;
        for l in links:
            ii = ii+1
            if(ii > mconfig['max_news_in_each_cata'] ):
                break;
            ares = get_artical_info(l)
            if not ares:
                continue
            ares['url'] = l
            ares['source'] = 'eisamay'
            ares['categories']= seed['categories']
            ares['tags']= seed['categories']
            ares['rand_id']= hashlib.sha224(ares['url']).hexdigest()#''.join(random.SystemRandom().choice(string.ascii_uppercase + string.digits) for _ in range(20)) 
            res.append(ares)
        return res
    except Exception, e:
        print 'Error(get_all_data_for_a_seed)',seed,str(e)
        return None
 
         
def test():
    x = get_artical_info('http://eisamay.indiatimes.com/state/no-permission-for-sez-in-west-bengal-/articleshow/53000667.cms')
    y = get_all_artical_links('http://eisamay.indiatimes.com/state/articlelist/15819609.cms');
    print len(y)
    print x
    
#test();

