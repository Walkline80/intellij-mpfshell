class MQTTException(Exception):
	"""

	"""

class MQTTClient:
	def __init__(self, client_id, server, port=0, password=None, keeyalive=0, ssl=False, ssl_params={}):
		"""
		Create a mqtt client object.

		:param client_id:
		:param server:
		:param port:
		:param password:
		:param keeyalive:
		:param ssl:
		:param ssl_params:
		"""
		...

	def connect(self, clean_session=True):
		"""
		Delay for given number of milliseconds, should be positive or 0.

		:param ms: Delay in milliseconds
		:type ms: int
		"""
		...

	def set_callback(self, f):
		"""

		:param f:
		:return:
		"""

	def set_last_will(self, topic, msg, retain=False, qos=0):
		"""

		:param topic:
		:param msg:
		:param retain:
		:param qos:
		:return:
		"""

	def disconnect(self):
		"""

		:return:
		"""

	def ping(self):
		"""

		:return:
		"""

	def publish(self, topic, msg, retain=False, qos=0):
		"""

		:param topic:
		:param msg:
		:param retain:
		:param qos:
		:return:
		"""

	def subscribe(self, topic, qos=0):
		"""

		:param topic:
		:param qos:
		:return:
		"""

	def wait_msg(self):
		"""

		:return:
		"""

	def check_msg(self):
		"""

		:return:
		"""
