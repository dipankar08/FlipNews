#This is resposible to do the common work for all New parser
import pdb
import random,string
from masterConfig import mconfig
import hashlib

import datetime
import time



def buildNormalizedArticalData(config,adt):

    adt['source'] = config.get('name')
    
    today = datetime.datetime.utcnow() + datetime.timedelta(hours=5,minutes=30)
    adt['date'] = today.strftime('%d-%m-%Y')
    adt['time'] = today.strftime('%I:%M %p')
    adt['epoch'] = time.time()

    adt['rand_id']= hashlib.sha224(adt['url']).hexdigest()
    
    return adt;

def get_all_data_for_a_seed(config):
    #pdb.set_trace()
    try:
        res = []        
        for seed in config['seeds']:        
            artical_url_list = config['handaler_list'](seed['url'])
            if artical_url_list:
               #Process artical list
               ii =0
               for artical_url in artical_url_list:
                    ii = ii + 1
                    if(ii > mconfig['max_news_in_each_cata'] ):
                        break;
                    
                    artical_data = config['handaler_info'](artical_url)
                    if artical_data:
                    
                        artical_data['categories']= seed['categories']
                        artical_data['tags']= seed.get('categories')
                        artical_data['url'] = artical_url
                        
                        artical_data = buildNormalizedArticalData(config,artical_data)
                        if artical_data:
                            res.append(artical_data)
                        else:
                            print 'Error: buildNormalizedArticalData failed'
                    else:
                        print 'ERROR: Not able to retive articla with URL : %s' %artical_url           
            else:
                print 'ERROR : No artical list found for %s' %seed['url']
        return res
    except Exception, e:
        print 'Error(get_all_data_for_a_seed)',seed,str(e)
        return None
 