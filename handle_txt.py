#!/usr/bin/python
# -*- coding: utf-8 -*-
# @Author: Guoxuenan
# @Date:   2016-03-17 15:48:21
# @Last Modified by:   Guoxuenan
# @Last Modified time: 2017-01-15 15:30:29
# <a href="/chanjarster/weixin-java-tools/tree/v1.3.2" class="css-truncate">
import sys
import urllib
import urllib2
import collections
def getHtml(url):
	try:
		a = urllib.urlopen(url).read()
		return a
	except Exception, e:
		info = sys.exc_info()
		print e    #打印捕获的异常对象
		print info #打印异常的完整数据

# # file1=open("G:\hit.txt","r")
def get_commit_ID(fileread,mark_of_ID):
	lists=[]
	sets=set()
	file2=open(fileread,'r')
	for x in file2.readlines():
		# print x
		try:
			if x.index(mark_of_ID,0,len(x)):
				index=x.index(mark_of_ID,0,len(x))
				tempid=x[index+len(mark_of_ID):len(x)-3]
				if tempid not in sets:
					sets.add(tempid)
					lists.append(tempid)
				# print x
		except Exception, e:
			info = sys.exc_info()
			pass
	# print len(lists),
	file2.close()

	return lists

def get_commit_version(fileread,mark_of_version):
	lists=[]
	sets=set()
	file2=open(fileread,'r')
	for x in file2.readlines():
		# print x
		try:
			if x.index(mark_of_version,0,len(x)):
				index=x.index(mark_of_version,0,len(x))
				end=x.index('"',index+len(mark_of_version),len(x))
				temp=x[index+len(mark_of_version):end]
				# print temp
				if temp not in sets:
					sets.add(temp)
					lists.append(temp)
		except Exception, e:
			info = sys.exc_info()
			pass
	# print len(lists),
	file2.close()

	return lists

def get_commit_time(fileread,mark_of_time):
	lists=[]
	sets=set()
	file2=open(fileread,'r')
	for x in file2.readlines():
		# print x
		try:
			if x.index(mark_of_time,0,len(x))!=-1:
				# print x
				index=x.index(mark_of_time,0,len(x))
				end=x.index('"',index+len(mark_of_time),len(x))
				temp=x[index+len(mark_of_time):end]
				# print index,len(mark_of_time),end
				if temp not in sets:
					sets.add(temp)
					lists.append(temp)
		except Exception, e:
			info = sys.exc_info()
			pass
	# print len(lists),
	file2.close()

	return lists

def getnewurl(fileread):
	file2=open(fileread,'r')
	for x in file2.readlines():
		# print "s*******"+x
		try:
			if x.index("Previous",0,len(x)):
				lens=x.index("Previous",0,len(x))
				if x[lens:].find("https://")!=-1:
					start=x[lens:].find("https://")+lens
					end = x[start:].find("\"")+start
					# print x[start:end]
					return x[start:end]
		except Exception, e:
			info = sys.exc_info()
			pass
	file2.close()
	return " "
def get_useful_information(fileread,filewrite,markof_link):
	file2=open(fileread,'r')
	file_temp=open(filewrite,'a+')
	count=0
	for  x in file2.readlines():
		if x.find(markof_link+"commit/")!=-1 or  x.find(markof_link+"releases/tag/")!=-1 or x.find("<relative-time datetime")!=-1:
			file_temp.write(x)
		# 	count=1
		# if count!=0 and count <=15:
		# 	file_temp.write(x)
		# 	count+=1
	file2.close()
	file_temp.close()

def update(newurl):
	if newurl==" ":
		return None;
	wget_html("./temp.txt",newurl)

	markof_link="href=\"/"+ newurl[start+4:end]
	# print "ssss-------------"+markof_link

	get_useful_information("./temp.txt","./temp1.txt",markof_link)

	url= getnewurl("./temp.txt")

	return update(url)


def clear_tempfile(filename):
	file2=open(filename,'w')
	file2.write("filename:"+filename+'\n')
	file2.close()

def wget_html(filename,url):
	file2     = open(filename,'w')
	print "downloading...",url
	html      = getHtml(url)
	file2.write(html)
	file2.close()
	return html
if __name__ == '__main__':
	print "GET the page..."
	# url="https://github.com/chanjarster/weixin-java-tools/tags"
	# url="https://github.com/larryli/PuTTY/tags"
	# url="https://github.com/Supervisor/supervisor/tags"
	# url="https://github.com/yusuke/twitter4j/tags"
	url="https://github.com/google/compile-testing/tags"

	start= url.find("com/")
	end=url.find("tags")
	User_and_repo=url[start+4:end]
	# print User_and_repo
	mark_of_ID= "href=\"/"+ User_and_repo +"commit/"
	# print mark_of_ID
	mark_of_version= "href=\"/"+ User_and_repo +"releases/tag/"
	# print mark_of_version
	mark_of_time ='''datetime="'''

	clear_tempfile("./temp1.txt")
	clear_tempfile("./result.txt")
	wget_html("./temp.txt",url)
	get_useful_information("./temp.txt","./temp1.txt","href=\"/"+ User_and_repo)

	newurl= getnewurl("./temp.txt")
	
	update(newurl)

	# -----------------------------------------------------

	commit_id= get_commit_ID("./temp1.txt",mark_of_ID)
	version  = get_commit_version("./temp1.txt",mark_of_version)

	timelist = get_commit_time("./temp1.txt",mark_of_time)
	# print len(commit_id),str(commit_id)
	# print len(version),str(version)

	if len(commit_id)== len(version) and len(version)== len(timelist):
		result_dict=collections.OrderedDict()
		for x in xrange(0,len(version)):
			result_dict[commit_id[x]]=timelist[x]+'\t'+version[x] 

		# -----------------------------------------------------
		file2=open("./result.txt",'a+')
		for key,item in result_dict.items():
			# file2.write(str(result_dict))
			file2.write(str(key)+"\t"+str(item)+'\n')
		print "Get "+str(len(result_dict))+" release ID ,the result in current dir ,filename:result.txt"
		file2.close()
