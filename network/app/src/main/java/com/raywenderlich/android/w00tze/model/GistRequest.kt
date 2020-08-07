package com.raywenderlich.android.w00tze.model

class GistRequest(val files: Map<String, GistFile>, val description: String, val public: Boolean = true)