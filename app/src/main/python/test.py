import os
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import io
import nltk
import numpy as np
from nltk import tokenize
from nltk.stem import WordNetLemmatizer
from nltk.tokenize import word_tokenize

stops = ['в', 'без', 'до', 'для', 'за', 'через', 'над', 'по', 'из', 'у', 'около', 'под', 'о', 'про', 'на', 'к', 'перед', 'при', 'с', 'между',
         'я', 'ты', 'мы', 'вы', 'он', 'она', 'оно', 'они', 'себя', 'мой', 'твой', 'наш', 'ваш', 'свой', 'этот', 'тот', 'его' , 'её' , 'их',
         'кто', 'что',  'а', 'и', 'но', 'да', 'или', 'либо', 'тоже', 'также', 'зато', 'если', 'хотя', 'пока',
         'чтобы', 'как', 'будто', 'словно', 'точно'
         ]

def toLower(text):
    """Перевод строки в нижний регистр

    Args:
        text (string): исходная строка

    Returns:
        string: строка text в нижнем регистре
    """
    return str(text).lower()

def remove_punctuation(text):
    """Удаление пунктуационных знаков из строки

    Args:
        text (string): исходная строка

    Returns:
        string: строка text без пунктуации
    """
    tokenizer = tokenize.RegexpTokenizer(r'\w+')
    text = tokenizer.tokenize(text)
    text = " ".join(text)
    return text

def remove_stop_words(text):
    """Удаление стоп-слов из строки

    Args:
        text (string): исходная строка

    Returns:
        string: строка text без стоп-слов
    """

    text = [w for w in text.split() if not w in stops]

    text = " ".join(text)

    return text

def recommend(df, title, genre, desc):
    """рекомендация на основе названия и жанра аудиокниги

    Args:
        title (string): название аудиокниги
        genre (string): жанр аудиокниги
    """
    data = df.loc[df['genre'] == genre]

    print(data)

    #сборс индексов. Изначально индексы вырваны из общей массы
    data.reset_index(level = 0, inplace = True)
    data['desc'] = data['desc'].fillna('')

    #серия - название, иднекс
    indices = pd.Series(data.index, index = data['title'])

    tf = TfidfVectorizer(analyzer='word', stop_words=stops)
    tfidf_matrix = tf.fit_transform(data['desc'])

    sg = cosine_similarity(tfidf_matrix, tfidf_matrix)
    idx = indices[title]


    sig = list(enumerate(sg[idx]))
    sig = sorted(sig, key=lambda x: x[1], reverse=True)
    sig = sig[1:7]
    book_indices = [i[0] for i in sig]

    return data[['img','title', 'author',  'desc', 'genre']].iloc[book_indices]

def main(title, genre, desc, author):
    filename = os.path.join(os.path.dirname(__file__), "audioBooks.csv")
    genre = genre.lower()

    print(genre)

    df = pd.read_csv(filename, delimiter=';')

    if not title in df['title'].unique() : df = df.append({ 'img': '',
                                                          'title' : title,
                                                          'author' : author,
                                                          'genre' : genre,
                                                          'desc' : desc,
                                                          } ,
                                                        ignore_index=True)

    df['cleaned_desc'] = df['desc'].apply(toLower)
    df['cleaned_desc'] = df.cleaned_desc.apply(func=remove_punctuation)
    df['cleaned_desc'] = df.cleaned_desc.apply(func=remove_stop_words)

    rec = recommend(df, title, genre, desc)

    return rec.values.tolist()