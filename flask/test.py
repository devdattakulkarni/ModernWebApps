import unittest
import requests

class TestMusicMarketPlaceAPI(unittest.TestCase):

	def test_insert_lesson(self):
		url = "http://localhost:5003/lessons"
		lesson = {}
		lesson['name'] = 'test1'
		lesson['demo_url'] = 'demo'
		lesson['timings'] = 'timings'
		lesson['instrument'] = 'instrument'
		lesson['days'] = 'days'
		resp = requests.post(url, json=lesson)
		self.assertIsNotNone(resp.json())
		resp_json = resp.json()
		print(resp_json)
		lessonId = resp_json['id']
		self.assertTrue(resp.status_code, "200")
		self.assertIsNotNone(lessonId)
		return resp_json

	def test_get_lesson(self):
		resp_json = self.test_insert_lesson()
		lessonId = resp_json['id']

		url = "http://localhost:5003/lessons/" + str(lessonId)
		resp = requests.get(url)
		self.assertTrue(resp.status_code, "200")

	def test_delete_lesson(self):
		resp_json = self.test_insert_lesson()
		lessonId = resp_json['id']

		url = "http://localhost:5003/lessons/" + str(lessonId)
		resp = requests.delete(url)
		self.assertTrue(resp.status_code, "200")

		self.test_get_lesson_not_found(lessonId=lessonId)

	def test_get_lesson_not_found(self, lessonId="lesson123"):
		url = "http://localhost:5003/lessons/" + str(lessonId)
		resp = requests.get(url)
		self.assertTrue(resp.status_code, "404")

	def test_update_lesson_not_found(self):
		url = "http://localhost:5003/lessons/lesson123"
		resp = requests.put(url, json={"name": "test1"})
		self.assertTrue(resp.status_code, "404")

	def test_delete_lesson_not_found(self):
		url = "http://localhost:5003/lessons/lesson123"
		resp = requests.delete(url)
		self.assertTrue(resp.status_code, "404")

	def test_get_lessons(self):
		url = "http://localhost:5003/lessons"
		resp = requests.get(url)
		print(resp)
		self.assertTrue(resp.status_code, "200")


if __name__ == '__main__':
    unittest.main()